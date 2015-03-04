package com.dynious.refinedrelocation.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.mods.IC2Helper;
import cpw.mods.fml.common.Optional;
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
    private boolean IC2registerChange = false;

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
        if (IC2registerChange)
        {
            if (energySink == null)
                IC2Helper.removeFromEnergyNet(this);
            else
                IC2Helper.addToEnergyNet(this);
            IC2registerChange = false;
        }
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

    @Optional.Method(modid = Mods.IC2_ID)
    @Override
    public IEnergySink getEnergySink()
    {
        TileEntity tile = worldObj.getTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IEnergySink)
        {
            if (!tile.equals(energySink))
            {
                saveSetEnergySink((IEnergySink) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IEnergySink) tile;
        }
        else
        {
            if (energySink != null)
            {
                saveSetEnergySink(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Optional.Method(modid = Mods.IC2_ID)
    public void saveSetEnergySink(IEnergySink energySink)
    {
        if (this.energySink == null && energySink != null)
        {
            this.energySink = energySink;
            if (!worldObj.isRemote)
            {
                IC2registerChange = true;
            }
        }
        else if (this.energySink != null)
        {
            if (energySink == null && !worldObj.isRemote)
            {
                IC2registerChange = true;
            }
            this.energySink = energySink;
        }
    }

    @Optional.Method(modid = Mods.COFH_ENERGY_API_ID)
    @Override
    public IEnergyReceiver getEnergyReceiver()
    {
        TileEntity tile = worldObj.getTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IEnergyReceiver)
        {
            if (!tile.equals(energyReceiver))
            {
                setEnergyReceiver((IEnergyReceiver) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IEnergyReceiver) tile;
        }
        else
        {
            if (energyReceiver != null)
            {
                setEnergyReceiver(null);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return null;
        }
    }

    @Optional.Method(modid = Mods.COFH_ENERGY_API_ID)
    @Override
    public IEnergyProvider getEnergyProvider()
    {
        TileEntity tile = worldObj.getTileEntity(xConnected, yConnected, zConnected);
        if (tile != null && tile instanceof IEnergyProvider)
        {
            if (!tile.equals(energyProvider))
            {
                setEnergyProvider((IEnergyProvider) tile);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            }
            return (IEnergyProvider) tile;
        }
        else
        {
            if (energyProvider != null)
            {
                setEnergyProvider(null);
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
