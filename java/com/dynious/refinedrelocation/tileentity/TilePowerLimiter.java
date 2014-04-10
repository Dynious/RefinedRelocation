package com.dynious.refinedrelocation.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cofh.api.energy.IEnergyHandler;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import cpw.mods.fml.common.Loader;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import universalelectricity.api.energy.IEnergyInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static cpw.mods.fml.common.Optional.*;

@InterfaceList(value = {
        @Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Energy"),
        @Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore"),
        @Interface(iface = "universalelectricity.api.energy.IEnergyInterface", modid = "UniversalElectricity")})
public class TilePowerLimiter extends TileEntity implements IPowerReceptor, IEnergySink, IEnergyHandler, IEnergyInterface, ILoopable
{
    protected ForgeDirection connectedDirection = ForgeDirection.UNKNOWN;
    protected ForgeDirection previousConnectedDirection = ForgeDirection.UNKNOWN;
    protected IPowerReceptor powerReceptor;
    protected IEnergySink energySink;
    protected IEnergyHandler energyHandler;
    protected IEnergyInterface energyInterface;
    protected TileEntity[] tiles = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
    public boolean blocksChanged = true;
    public float maxAcceptedEnergy = 1;

    public void setConnectedSide(int connectedSide)
    {
        this.connectedDirection = ForgeDirection.getOrientation(connectedSide);
        this.blocksChanged = true;
        if (worldObj != null)
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
    }

    public ForgeDirection getConnectedDirection()
    {
        return connectedDirection;
    }

    public void setPowerReceptor(IPowerReceptor powerReceptor)
    {
        this.powerReceptor = powerReceptor;
    }

    public void setEnergyHandler(IEnergyHandler energyHandler)
    {
        this.energyHandler = energyHandler;
    }

    public void setEnergySink(IEnergySink energySink)
    {
        if (this.energySink == null && energySink != null)
        {
            this.energySink = energySink;
            if (!worldObj.isRemote)
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            }
        }
        else if (this.energySink != null)
        {
            if (energySink == null && !worldObj.isRemote)
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            }
            this.energySink = energySink;
        }
    }

    public void setEnergyInterface(IEnergyInterface energyInterface)
    {
        this.energyInterface = energyInterface;
    }

    public IPowerReceptor getPowerReceptor()
    {
        return powerReceptor;
    }

    public IEnergySink getEnergySink()
    {
        return energySink;
    }

    public IEnergyHandler getEnergyHandler()
    {
        return energyHandler;
    }

    public IEnergyInterface getEnergyInterface()
    {
        return energyInterface;
    }

    public TileEntity[] getTiles()
    {
        return tiles;
    }

    @Override
    public void invalidate()
    {
        if (this.getEnergySink() != null && !worldObj.isRemote)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        if (this.getEnergySink() != null && !worldObj.isRemote)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.onChunkUnload();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (canConnect())
        {
            TileEntity tile = null;

            if (connectedDirection != previousConnectedDirection)
            {
                //Look up the tile we are connected to
                tile = getConnectedTile();

                resetConnections();
                checkConnectedDirection(tile);
                previousConnectedDirection = connectedDirection;
                worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }

            if (blocksChanged)
            {
                //If we haven't looked up the tile we are connected to, do that
                if (tile == null)
                {
                    tile = getConnectedTile();
                }

                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                {
                    if (direction != connectedDirection)
                    {
                        tiles[direction.ordinal()] = DirectionHelper.getTileAtSide(this, direction);
                    }
                }
                if (tile == null)
                {
                    resetConnections();
                    worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
                }
                else
                {
                    checkConnectedDirection(tile);
                }

                blocksChanged = false;
            }
        }
    }

    protected void checkConnectedDirection(TileEntity tile)
    {
        if (tile != null && !isLooping(tile))
        {
            boolean updated = false;
            if (Loader.isModLoaded("BuildCraft|Energy") && tile instanceof IPowerReceptor)
            {
                if (getPowerReceptor() == null)
                {
                    updated = true;
                }
                setPowerReceptor((IPowerReceptor) tile);
            }
            if (Loader.isModLoaded("IC2") && tile instanceof IEnergySink)
            {
                if (getEnergySink() == null)
                {
                    updated = true;
                }
                setEnergySink((IEnergySink) tile);
            }
            if (Loader.isModLoaded("CoFHCore") && tile instanceof IEnergyHandler)
            {
                if (getEnergyHandler() == null)
                {
                    updated = true;
                }
                setEnergyHandler((IEnergyHandler) tile);
            }
            if (Loader.isModLoaded("UniversalElectricity") && tile instanceof IEnergyInterface)
            {
                if (getEnergyInterface() == null)
                {
                    updated = true;
                }
                setEnergyInterface((IEnergyInterface) tile);
            }
            if (updated || tile instanceof TileBlockExtender)
            {
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
        }
    }

    protected void resetConnections()
    {
        setPowerReceptor(null);
        setEnergySink(null);
        setEnergyHandler(null);
        setEnergyInterface(null);
    }

    public boolean hasConnection()
    {
        if (Loader.isModLoaded("BuildCraft|Energy") && getPowerReceptor() != null)
        {
            return true;
        }
        if (Loader.isModLoaded("IC2") && getEnergySink() != null)
        {
            return true;
        }
        if (Loader.isModLoaded("CoFHCore") && getEnergyHandler() != null)
        {
            return true;
        }
        if (Loader.isModLoaded("UniversalElectricity") && getEnergyInterface() != null)
        {
            return true;
        }
        return false;
    }

    public List<String> getConnectionTypes()
    {
        List<String> connections = new ArrayList<String>();

        if (Loader.isModLoaded("BuildCraft|Energy") && getPowerReceptor() != null)
            connections.add("Buildcraft Energy");
        if (Loader.isModLoaded("IC2") && getEnergySink() != null)
            connections.add("IC2 Energy");
        if (Loader.isModLoaded("CoFHCore") && getEnergyHandler() != null)
            connections.add("Thermal Expansion Energy");
        if (Loader.isModLoaded("UniversalElectricity") && getEnergyInterface() != null)
            connections.add("Universal Electricity Energy");

        return connections;
    }

    public boolean canConnect()
    {
        return connectedDirection != ForgeDirection.UNKNOWN;
    }

    public TileEntity getConnectedTile()
    {
        return DirectionHelper.getTileAtSide(this, connectedDirection);
    }

    private boolean isLooping(TileEntity tile)
    {
        return tile != null && tile instanceof ILoopable && isTileConnectedToThis((ILoopable) tile, new ArrayList<ILoopable>());
    }

    private boolean isTileConnectedToThis(ILoopable loopable, List<ILoopable> visited)
    {
        boolean isLooping;
        TileEntity tile = loopable.getConnectedTile();
        if (tile == this || visited.contains(tile))
        {
            return true;
        }
        if (tile != null && tile instanceof ILoopable)
        {
            visited.add((ILoopable) tile);
            isLooping = isTileConnectedToThis((ILoopable) tile, visited);
        }
        else
        {
            return false;
        }
        return isLooping;
    }

    /*
    Power interaction
     */

    @Method(modid = "BuildCraft|Energy")
    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection forgeDirection)
    {
        if (getPowerReceptor() != null)
        {
            return getPowerReceptor().getPowerReceiver(forgeDirection.getOpposite());
        }
        return null;
    }

    @Method(modid = "BuildCraft|Energy")
    @Override
    public void doWork(PowerHandler powerHandler)
    {
        if (getPowerReceptor() != null)
        {
            getPowerReceptor().doWork(powerHandler);
        }
    }

    @Method(modid = "BuildCraft|Energy")
    @Override
    public World getWorld()
    {
        if (getPowerReceptor() != null)
        {
            return getPowerReceptor().getWorld();
        }
        return null;
    }

    @Method(modid = "IC2")
    @Override
    public double demandedEnergyUnits()
    {
        if (getEnergySink() != null)
        {
            return getEnergySink().demandedEnergyUnits();
        }
        return 0;
    }

    @Method(modid = "IC2")
    @Override
    public double injectEnergyUnits(ForgeDirection forgeDirection, double v)
    {
        if (getEnergySink() != null)
        {
            double storedEnergy = 0D;
            if (v > maxAcceptedEnergy)
            {
                storedEnergy = v - maxAcceptedEnergy;
                v = maxAcceptedEnergy;
            }
            return getEnergySink().injectEnergyUnits(forgeDirection.getOpposite(), v) + storedEnergy;
        }
        return 0;
    }

    @Method(modid = "IC2")
    @Override
    public int getMaxSafeInput()
    {
        if (getEnergySink() != null)
        {
            return getEnergySink().getMaxSafeInput();
        }
        return 0;
    }

    @Method(modid = "IC2")
    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection)
    {
        return getEnergySink() != null && getEnergySink().acceptsEnergyFrom(tileEntity, forgeDirection.getOpposite());
    }

    @Method(modid = "CoFHCore")
    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        if (getEnergyHandler() != null)
        {
            int storedEnergy = 0;
            if (i > maxAcceptedEnergy)
            {
                storedEnergy = i - (int) maxAcceptedEnergy;
                i = (int) maxAcceptedEnergy;
            }
            return getEnergyHandler().receiveEnergy(forgeDirection.getOpposite(), i, b) + storedEnergy;
        }
        return 0;
    }

    @Method(modid = "CoFHCore")
    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        if (getEnergyHandler() != null)
        {
            return getEnergyHandler().extractEnergy(forgeDirection.getOpposite(), i, b);
        }
        return 0;
    }

    @Method(modid = "CoFHCore")
    @Override
    public boolean canInterface(ForgeDirection forgeDirection)
    {
        return getEnergyHandler() != null && getEnergyHandler().canInterface(forgeDirection.getOpposite());
    }

    @Method(modid = "CoFHCore")
    @Override
    public int getEnergyStored(ForgeDirection forgeDirection)
    {
        if (getEnergyHandler() != null)
        {
            return getEnergyHandler().getEnergyStored(forgeDirection.getOpposite());
        }
        return 0;
    }

    @Method(modid = "CoFHCore")
    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection)
    {
        if (getEnergyHandler() != null)
        {
            return getEnergyHandler().getMaxEnergyStored(forgeDirection.getOpposite());
        }
        return 0;
    }

    @Method(modid = "UniversalElectricity")
    @Override
    public long onReceiveEnergy(ForgeDirection direction, long l, boolean b)
    {
        if (getEnergyInterface() != null)
        {
            long storedEnergy = 0;
            if (l > maxAcceptedEnergy)
            {
                storedEnergy = l - (long) maxAcceptedEnergy;
                l = (long) maxAcceptedEnergy;
            }
            return getEnergyInterface().onReceiveEnergy(direction.getOpposite(), l, b) + storedEnergy;
        }
        return 0;
    }

    @Method(modid = "UniversalElectricity")
    @Override
    public long onExtractEnergy(ForgeDirection direction, long l, boolean b)
    {
        if (getEnergyInterface() != null)
        {
            return getEnergyInterface().onExtractEnergy(direction.getOpposite(), l, b);
        }
        return 0;
    }

    @Method(modid = "UniversalElectricity")
    @Override
    public boolean canConnect(ForgeDirection direction, Object o)
    {
        if (getEnergyInterface() != null)
        {
            return getEnergyInterface().canConnect(direction.getOpposite(), o);
        }
        return false;
    }

    /*
    NBT stuffs
     */

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        setConnectedSide(compound.getByte("side"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("side", (byte) connectedDirection.ordinal());
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        setConnectedSide(pkt.data.getByte("side"));
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("side", (byte) connectedDirection.ordinal());
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, compound);
    }

    public boolean rotateBlock()
    {
        setConnectedSide((getConnectedDirection().ordinal() + 1) % ForgeDirection.VALID_DIRECTIONS.length);
        return true;
    }
}
