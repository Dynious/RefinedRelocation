package com.dynious.refinedrelocation.tileentity;

import buildcraft.api.power.IPowerReceptor;
import cofh.api.energy.IEnergyHandler;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import org.apache.commons.lang3.ArrayUtils;
import universalelectricity.api.energy.IEnergyInterface;

public class TileWirelessBlockExtender extends TileAdvancedFilteredBlockExtender
{
    public int xConnected = Integer.MAX_VALUE;
    public int yConnected = Integer.MAX_VALUE;
    public int zConnected = Integer.MAX_VALUE;
    private int recheckTime = 0;

    public void setLink(int x, int y, int z)
    {
        this.xConnected = x;
        this.yConnected = y;
        this.zConnected = z;
        this.blocksChanged = true;
        if (worldObj != null)
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void clearLink()
    {
        setLink(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public boolean isLinked()
    {
        return this.xConnected != Integer.MAX_VALUE;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        recheckTime++;
        if (recheckTime >= 20)
        {
            checkConnectedDirection(worldObj.getBlockTileEntity(xConnected, yConnected, zConnected));
            recheckTime = 0;
        }
    }

    @Override
    public void setConnectedSide(int connectedSide)
    {
    }

    @Override
    public ForgeDirection getConnectedDirection()
    {
        return ForgeDirection.UNKNOWN;
    }

    @Override
    public boolean canConnect()
    {
        return true;
    }

    @Override
    public boolean canDisguise()
    {
        return false;
    }


    @Override
    public TileEntity getConnectedTile()
    {
        return worldObj.getBlockTileEntity(xConnected, yConnected, zConnected);
    }

    @Override
    public boolean isRedstoneTransmissionActive()
    {
        // never render as if redstone is active
        return false;
    }

    @Override
    public boolean isRedstoneTransmissionEnabled()
    {
        // always render as if redstone is enabled
        return true;
    }

    @Override
    public IInventory getInventory()
    {
        TileEntity tile = worldObj.getBlockTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IInventory)
        {
            if (!tile.equals(inventory))
            {
                setInventory((IInventory) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IInventory) tile;
        }
        else
        {
            if (inventory != null)
            {
                setInventory(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Override
    public IFluidHandler getFluidHandler()
    {
        TileEntity tile = worldObj.getBlockTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IFluidHandler)
        {
            if (!tile.equals(fluidHandler))
            {
                setFluidHandler((IFluidHandler) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IFluidHandler) tile;
        }
        else
        {
            if (fluidHandler != null)
            {
                setFluidHandler(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Override
    public IPowerReceptor getPowerReceptor()
    {
        TileEntity tile = worldObj.getBlockTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IPowerReceptor)
        {
            if (!tile.equals(powerReceptor))
            {
                setPowerReceptor((IPowerReceptor) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IPowerReceptor) tile;
        }
        else
        {
            if (powerReceptor != null)
            {
                setPowerReceptor(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Override
    public IEnergySink getEnergySink()
    {
        TileEntity tile = worldObj.getBlockTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IEnergySink)
        {
            if (!tile.equals(energySink))
            {
                setEnergySink((IEnergySink) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IEnergySink) tile;
        }
        else
        {
            if (energySink != null)
            {
                setEnergySink(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Override
    public IEnergyHandler getEnergyHandler()
    {
        TileEntity tile = worldObj.getBlockTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IEnergyHandler)
        {
            if (!tile.equals(energyHandler))
            {
                setEnergyHandler((IEnergyHandler) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IEnergyHandler) tile;
        }
        else
        {
            if (energyHandler != null)
            {
                setEnergyHandler(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Override
    public IEnergyInterface getEnergyInterface()
    {
        TileEntity tile = worldObj.getBlockTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IEnergyInterface)
        {
            if (!tile.equals(energyInterface))
            {
                setEnergyInterface((IEnergyInterface) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IEnergyInterface) tile;
        }
        else
        {
            if (energyInterface != null)
            {
                setEnergyInterface(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        setLink(pkt.data.getInteger("xConnected"), pkt.data.getInteger("yConnected"), pkt.data.getInteger("zConnected"));
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("xConnected", xConnected);
        compound.setInteger("yConnected", yConnected);
        compound.setInteger("zConnected", zConnected);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        setLink(compound.getInteger("xConnected"), compound.getInteger("yConnected"), compound.getInteger("zConnected"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("xConnected", xConnected);
        compound.setInteger("yConnected", yConnected);
        compound.setInteger("zConnected", zConnected);
    }
}
