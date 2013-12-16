package com.dynious.blex.tileentity;

import buildcraft.api.power.IPowerReceptor;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Loader;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

public class TileWirelessBlockExtender extends TileAdvancedFilteredBlockExtender
{
    public int xConnected = Integer.MAX_VALUE;
    public int yConnected = Integer.MAX_VALUE;
    public int zConnected = Integer.MAX_VALUE;

    public void setConnection(int x, int y, int z)
    {
        this.xConnected = x;
        this.yConnected = y;
        this.zConnected = z;
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
    public void updateEntity()
    {
        super.updateEntity();
        if (xConnected != Integer.MAX_VALUE)
        {
            TileEntity tile = worldObj.getBlockTileEntity(xConnected, yConnected, zConnected);
            if (!hasConnection())
            {
                checkConnectedDirection(tile);
            }
            else if (tile == null)
            {
                setInventory(null);
                setFluidHandler(null);
                setPowerReceptor(null);
                setEnergySink(null);
                setEnergyHandler(null);
            }
            recheckTiles++;
            if (recheckTiles >= 20)
            {
                checkConnectedDirection(tile);
                recheckTiles = 0;
            }
        }
        if (blocksChanged)
        {
            for (ForgeDirection direction : ForgeDirection.values())
            {
                if (direction != connectedDirection)
                {
                    tiles[direction.ordinal()] = worldObj.getBlockTileEntity(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
                }
            }
            blocksChanged = false;
        }
        if (lightAmount > 0F)
        {
            lightAmount = lightAmount - 0.01F;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        xConnected = compound.getInteger("xConnected");
        yConnected = compound.getInteger("yConnected");
        zConnected = compound.getInteger("zConnected");
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
