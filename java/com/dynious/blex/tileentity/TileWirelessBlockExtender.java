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
    private int xConnected = Integer.MAX_VALUE;
    private int yConnected = Integer.MAX_VALUE;
    private int zConnected = Integer.MAX_VALUE;

    public void setConnection(int x, int y, int z)
    {
        System.out.println(x + ":" + y + ":" +  z);
        this.xConnected = x;
        this.yConnected = y;
        this.zConnected = z;
    }

    @Override
    public void setConnectedSide(int connectedSide){}

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
                if (tile != null && !(tile instanceof TileBlockExtender))
                {
                    if (tile instanceof IInventory)
                    {
                        setInventory((IInventory) tile);
                    }
                    if (tile instanceof IFluidHandler)
                    {
                        setFluidHandler((IFluidHandler) tile);
                    }
                    if (Loader.isModLoaded("BuildCraft|Energy") && tile instanceof IPowerReceptor)
                    {
                        setPowerReceptor((IPowerReceptor) tile);
                    }
                    if (Loader.isModLoaded("IC2") && tile instanceof IEnergySink)
                    {
                        setEnergySink((IEnergySink) tile);
                    }
                    if (Loader.isModLoaded("CoFHCore") && tile instanceof IEnergyHandler)
                    {
                        setEnergyHandler((IEnergyHandler) tile);
                    }
                }
            }
            else if (tile == null)
            {
                setInventory(null);
                setFluidHandler(null);
                setPowerReceptor(null);
                setEnergySink(null);
                setEnergyHandler(null);
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
