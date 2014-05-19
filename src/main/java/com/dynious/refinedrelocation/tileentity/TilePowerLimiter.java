package com.dynious.refinedrelocation.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cofh.api.energy.IEnergyHandler;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.helper.EnergyType;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.mods.IC2Helper;
import com.dynious.refinedrelocation.tileentity.energy.TileUniversalElectricity;
import cpw.mods.fml.common.Loader;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.energy.IEnergyInterface;

import java.util.ArrayList;
import java.util.List;

import static cpw.mods.fml.common.Optional.Method;

public class TilePowerLimiter extends TileUniversalElectricity implements ILoopable
{
    protected ForgeDirection connectedDirection = ForgeDirection.UNKNOWN;
    protected ForgeDirection previousConnectedDirection = ForgeDirection.UNKNOWN;
    protected IPowerReceptor powerReceptor;
    protected IEnergySink energySink;
    protected IEnergyHandler energyHandler;
    protected IEnergyInterface energyInterface;
    protected TileEntity[] tiles = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
    public boolean blocksChanged = true;
    private double maxAcceptedEnergy = 10;
    private boolean disablePower = false;
    private boolean redstoneToggle = false;
    private boolean oldState = false;

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
                IC2Helper.addToEnergyNet(this);
            }
        }
        else if (this.energySink != null)
        {
            if (energySink == null && !worldObj.isRemote)
            {
                IC2Helper.removeFromEnergyNet(this);
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

    public double getMaxAcceptedEnergy()
    {
        return maxAcceptedEnergy;
    }

    public void setMaxAcceptedEnergy(double value)
    {
        maxAcceptedEnergy = value;
    }

    public void setDisablePower(boolean value)
    {
        if (value != disablePower)
        {
            disablePower = value;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public boolean getDisablePower()
    {
        return disablePower || (!redstoneToggle && worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord));
    }

    public void setRedstoneToggle(boolean toggle)
    {
        redstoneToggle = toggle;
    }

    public boolean getRedstoneToggle()
    {
        return redstoneToggle;
    }

    public void newRedstoneState(boolean state)
    {
        if (!oldState && state)
        {
            setDisablePower(!disablePower);
        }
        oldState = state;
    }

    @Override
    public void invalidate()
    {
        if (this.getEnergySink() != null && !worldObj.isRemote)
        {
            IC2Helper.removeFromEnergyNet(this);
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        if (this.getEnergySink() != null && !worldObj.isRemote)
        {
            IC2Helper.removeFromEnergyNet(this);
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

                if (redstoneToggle)
                {
                    newRedstoneState(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord));
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
            if (Mods.IS_BC_ENERGY_LOADED && tile instanceof IPowerReceptor)
            {
                if (getPowerReceptor() == null)
                {
                    updated = true;
                }
                setPowerReceptor((IPowerReceptor) tile);
            }
            if (Mods.IS_IC2_LOADED && tile instanceof IEnergySink)
            {
                if (getEnergySink() == null)
                {
                    updated = true;
                }
                setEnergySink((IEnergySink) tile);
            }
            if (Mods.IS_COFH_CORE_LOADED && tile instanceof IEnergyHandler)
            {
                if (getEnergyHandler() == null)
                {
                    updated = true;
                }
                setEnergyHandler((IEnergyHandler) tile);
            }
            if (Mods.IS_UE_LOADED && tile instanceof IEnergyInterface)
            {
                if (getEnergyInterface() == null)
                {
                    updated = true;
                }
                setEnergyInterface((IEnergyInterface) tile);
            }
            if (updated || tile instanceof ILoopable)
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

    public List<EnergyType> getConnectionTypes()
    {
        List<EnergyType> connections = new ArrayList<EnergyType>();

        if (Mods.IS_BC_ENERGY_LOADED && getPowerReceptor() != null)
            connections.add(EnergyType.MJ);
        if (Mods.IS_IC2_LOADED && getEnergySink() != null)
            connections.add(EnergyType.EU);
        if (Mods.IS_COFH_CORE_LOADED && getEnergyHandler() != null)
            connections.add(EnergyType.RF);
        if (Mods.IS_UE_LOADED && getEnergyInterface() != null)
            connections.add(EnergyType.KJ);

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
        if (getPowerReceptor() != null && !getDisablePower())
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
        if (getEnergySink() != null && !getDisablePower())
        {
            double demanded =  getEnergySink().demandedEnergyUnits();
            if (demanded > EnergyType.EU.fromInternal(maxAcceptedEnergy))
            {
                demanded = EnergyType.EU.fromInternal(maxAcceptedEnergy);
            }
            return demanded;
        }
        return 0;
    }

    @Method(modid = "IC2")
    @Override
    public double injectEnergyUnits(ForgeDirection forgeDirection, double v)
    {
        if (getEnergySink() != null && !getDisablePower())
        {
            double storedEnergy = 0D;
            if (v > EnergyType.EU.fromInternal(maxAcceptedEnergy))
            {
                storedEnergy = v - EnergyType.EU.fromInternal(maxAcceptedEnergy);
                v = EnergyType.EU.fromInternal(maxAcceptedEnergy);
            }
            return getEnergySink().injectEnergyUnits(forgeDirection.getOpposite(), v) + storedEnergy;
        }
        return v;
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
        if (getEnergyHandler() != null  && !getDisablePower())
        {
            int storedEnergy = 0;
            if (i > EnergyType.RF.fromInternal(maxAcceptedEnergy))
            {
                storedEnergy = i - (int) EnergyType.RF.fromInternal(maxAcceptedEnergy);
                i = (int) EnergyType.RF.fromInternal(maxAcceptedEnergy);
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
        if (getEnergyInterface() != null  && !getDisablePower())
        {
            long storedEnergy = 0;
            if (l > EnergyType.KJ.fromInternal(maxAcceptedEnergy))
            {
                storedEnergy = l - (long) EnergyType.KJ.fromInternal(maxAcceptedEnergy);
                l = (long) EnergyType.KJ.fromInternal(maxAcceptedEnergy);
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
        setMaxAcceptedEnergy(compound.getDouble("maxEnergy"));
        setDisablePower(compound.getBoolean("disablePower"));
        setRedstoneToggle(compound.getBoolean("toggle"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("side", (byte) connectedDirection.ordinal());
        compound.setDouble("maxEnergy", maxAcceptedEnergy);
        compound.setBoolean("disablePower", getDisablePower());
        compound.setBoolean("toggle", getRedstoneToggle());
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        setConnectedSide(pkt.data.getByte("side"));
        setDisablePower(pkt.data.getBoolean("disablePower"));
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("side", (byte) connectedDirection.ordinal());
        compound.setBoolean("disablePower", getDisablePower());
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, compound);
    }

    public boolean rotateBlock()
    {
        setConnectedSide((getConnectedDirection().ordinal() + 1) % ForgeDirection.VALID_DIRECTIONS.length);
        return true;
    }
}
