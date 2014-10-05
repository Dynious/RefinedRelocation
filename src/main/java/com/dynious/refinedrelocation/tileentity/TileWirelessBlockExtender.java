package com.dynious.refinedrelocation.tileentity;

import buildcraft.api.power.IPowerReceptor;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

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
            checkConnectedDirection(worldObj.getTileEntity(xConnected, yConnected, zConnected));
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
        return worldObj.getTileEntity(xConnected, yConnected, zConnected);
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
        TileEntity tile = worldObj.getTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IInventory)
        {
            if (!tile.equals(inventory))
            {
                setInventory((IInventory) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IInventory) tile;
        }
        else
        {
            if (inventory != null)
            {
                setInventory(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Override
    public IFluidHandler getFluidHandler()
    {
        TileEntity tile = worldObj.getTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IFluidHandler)
        {
            if (!tile.equals(fluidHandler))
            {
                setFluidHandler((IFluidHandler) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IFluidHandler) tile;
        }
        else
        {
            if (fluidHandler != null)
            {
                setFluidHandler(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Override
    public IPowerReceptor getPowerReceptor()
    {
        TileEntity tile = worldObj.getTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IPowerReceptor)
        {
            if (!tile.equals(powerReceptor))
            {
                setPowerReceptor((IPowerReceptor) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IPowerReceptor) tile;
        }
        else
        {
            if (powerReceptor != null)
            {
                setPowerReceptor(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Override
    public IEnergySink getEnergySink()
    {
        TileEntity tile = worldObj.getTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IEnergySink)
        {
            if (!tile.equals(energySink))
            {
                setEnergySink((IEnergySink) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IEnergySink) tile;
        }
        else
        {
            if (energySink != null)
            {
                setEnergySink(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        setLink(pkt.func_148857_g().getInteger("xConnected"), pkt.func_148857_g().getInteger("yConnected"), pkt.func_148857_g().getInteger("zConnected"));
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("xConnected", xConnected);
        compound.setInteger("yConnected", yConnected);
        compound.setInteger("zConnected", zConnected);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, compound);
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
