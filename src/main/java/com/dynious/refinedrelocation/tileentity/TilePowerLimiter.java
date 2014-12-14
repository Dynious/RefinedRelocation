package com.dynious.refinedrelocation.tileentity;

import cofh.api.energy.IEnergyHandler;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.helper.EnergyType;
import com.dynious.refinedrelocation.helper.LoopHelper;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.mods.IC2Helper;
import com.dynious.refinedrelocation.tileentity.energy.TileIndustrialCraft;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cpw.mods.fml.common.Optional.Method;

public class TilePowerLimiter extends TileIndustrialCraft implements ILoopable
{
    public boolean blocksChanged = true;
    protected ForgeDirection connectedDirection = ForgeDirection.UNKNOWN;
    protected ForgeDirection previousConnectedDirection = ForgeDirection.UNKNOWN;
    protected IEnergySink energySink;
    protected IEnergyHandler energyHandler;
    private double maxAcceptedEnergy = 10;
    private boolean disablePower = false;
    private boolean redstoneToggle = false;
    private boolean oldState = false;

    public void setConnectedSide(int connectedSide)
    {
        this.connectedDirection = ForgeDirection.getOrientation(connectedSide);
        this.blocksChanged = true;
        if (worldObj != null)
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
    }

    public ForgeDirection getConnectedDirection()
    {
        return connectedDirection;
    }

    public IEnergySink getEnergySink()
    {
        return energySink;
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

    public IEnergyHandler getEnergyHandler()
    {
        return energyHandler;
    }

    public void setEnergyHandler(IEnergyHandler energyHandler)
    {
        this.energyHandler = energyHandler;
    }

    public double getMaxAcceptedEnergy()
    {
        return maxAcceptedEnergy;
    }

    public void setMaxAcceptedEnergy(double value)
    {
        maxAcceptedEnergy = value;
    }

    public boolean getDisablePower()
    {
        return disablePower;
    }

    public void setDisablePower(boolean value)
    {
        if (value != disablePower)
        {
            disablePower = value;
            if (worldObj != null)
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public boolean getRedstoneToggle()
    {
        return redstoneToggle;
    }

    public void setRedstoneToggle(boolean toggle)
    {
        redstoneToggle = toggle;
        if (worldObj != null && !worldObj.isRemote)
            newRedstoneState(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord));
    }

    public void newRedstoneState(boolean state)
    {
        if (redstoneToggle)
        {
            if (!oldState && state)
            {
                setDisablePower(!disablePower);
            }
            oldState = state;
        }
        else
        {
            setDisablePower(state);
        }
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

                if (tile == null)
                {
                    resetConnections();
                    worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
                }
                else
                {
                    checkConnectedDirection(tile);
                }

                if (!worldObj.isRemote)
                    newRedstoneState(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord));
                blocksChanged = false;
            }
        }
    }

    protected void checkConnectedDirection(TileEntity tile)
    {
        if (tile != null && !LoopHelper.isLooping(this, tile))
        {
            boolean updated = false;
            if (Mods.IS_IC2_LOADED && tile instanceof IEnergySink)
            {
                if (getEnergySink() == null)
                {
                    updated = true;
                }
                setEnergySink((IEnergySink) tile);
            }
            if (Mods.IS_COFH_ENERGY_API_LOADED && tile instanceof IEnergyHandler)
            {
                if (getEnergyHandler() == null)
                {
                    updated = true;
                }
                setEnergyHandler((IEnergyHandler) tile);
            }
            if (updated)
            {
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
        }
    }

    protected void resetConnections()
    {
        setEnergySink(null);
        setEnergyHandler(null);
    }

    public List<EnergyType> getConnectionTypes()
    {
        List<EnergyType> connections = new ArrayList<EnergyType>();
        if (Mods.IS_IC2_LOADED && getEnergySink() != null)
            connections.add(EnergyType.EU);
        if (Mods.IS_COFH_ENERGY_API_LOADED && getEnergyHandler() != null)
            connections.add(EnergyType.RF);

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

    public List<TileEntity> getConnectedTiles()
    {
        return Arrays.asList(DirectionHelper.getTileAtSide(this, connectedDirection));
    }

    /*
    Power interaction
     */

    @Method(modid = Mods.IC2_ID)
    @Override
    public double getDemandedEnergy()
    {
        if (getEnergySink() != null && !getDisablePower())
        {
            double demanded = getEnergySink().getDemandedEnergy();
            if (demanded > EnergyType.EU.fromInternal(maxAcceptedEnergy))
            {
                demanded = EnergyType.EU.fromInternal(maxAcceptedEnergy);
            }
            return demanded;
        }
        return 0;
    }

    @Method(modid = Mods.IC2_ID)
    @Override
    public double injectEnergy(ForgeDirection forgeDirection, double v, double v2)
    {
        if (getEnergySink() != null && !getDisablePower())
        {
            double storedEnergy = 0D;
            if (v > EnergyType.EU.fromInternal(maxAcceptedEnergy))
            {
                storedEnergy = v - EnergyType.EU.fromInternal(maxAcceptedEnergy);
                v = EnergyType.EU.fromInternal(maxAcceptedEnergy);
            }
            return getEnergySink().injectEnergy(connectedDirection.getOpposite(), v, v2) + storedEnergy;
        }
        return v;
    }

    @Method(modid = Mods.IC2_ID)
    @Override
    public int getSinkTier()
    {
        if (getEnergySink() != null)
        {
            return getEnergySink().getSinkTier();
        }
        return 0;
    }

    @Method(modid = Mods.IC2_ID)
    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection)
    {
        return getEnergySink() != null && getEnergySink().acceptsEnergyFrom(tileEntity, connectedDirection.getOpposite());
    }

    @Method(modid = Mods.COFH_ENERGY_API_ID)
    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        if (getEnergyHandler() != null && !getDisablePower())
        {
            if (i > EnergyType.RF.fromInternal(maxAcceptedEnergy))
            {
                i = (int) EnergyType.RF.fromInternal(maxAcceptedEnergy);
            }
            return getEnergyHandler().receiveEnergy(connectedDirection.getOpposite(), i, b);
        }
        return 0;
    }

    @Method(modid = Mods.COFH_ENERGY_API_ID)
    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        if (getEnergyHandler() != null)
        {
            return getEnergyHandler().extractEnergy(connectedDirection.getOpposite(), i, b);
        }
        return 0;
    }

    @Method(modid = Mods.COFH_ENERGY_API_ID)
    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection)
    {
        return getEnergyHandler() != null && getEnergyHandler().canConnectEnergy(connectedDirection.getOpposite());
    }

    @Method(modid = Mods.COFH_ENERGY_API_ID)
    @Override
    public int getEnergyStored(ForgeDirection forgeDirection)
    {
        if (getEnergyHandler() != null)
        {
            return getEnergyHandler().getEnergyStored(connectedDirection.getOpposite());
        }
        return 0;
    }

    @Method(modid = Mods.COFH_ENERGY_API_ID)
    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection)
    {
        if (getEnergyHandler() != null)
        {
            return getEnergyHandler().getMaxEnergyStored(connectedDirection.getOpposite());
        }
        return 0;
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
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        setConnectedSide(pkt.func_148857_g().getByte("side"));
        setDisablePower(pkt.func_148857_g().getBoolean("disablePower"));
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("side", (byte) connectedDirection.ordinal());
        compound.setBoolean("disablePower", getDisablePower());
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, compound);
    }

    public boolean rotateBlock()
    {
        setConnectedSide((getConnectedDirection().ordinal() + 1) % ForgeDirection.VALID_DIRECTIONS.length);
        return true;
    }
}
