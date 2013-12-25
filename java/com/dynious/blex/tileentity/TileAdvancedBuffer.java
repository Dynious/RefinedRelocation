package com.dynious.blex.tileentity;

import buildcraft.api.transport.IPipeTile;
import cofh.api.transport.IItemConduit;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileAdvancedBuffer extends TileBuffer
{
    private byte[] insertDirection = {0, 1, 2, 3, 4, 5};

    public byte[] getInsertDirection()
    {
        return insertDirection;
    }

    public void setInsertDirection(int from, int value)
    {
        if (value > 5)
            value = 0;
        insertDirection[from] = (byte) value;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack)
    {
        for (int i : insertDirection)
        {
            if (i >= tiles.length || i == slot)
                continue;
            TileEntity tile = tiles[i];
            if (tile != null)
            {
                if (Loader.isModLoaded("CoFHCore") && tile instanceof IItemConduit)
                {
                    ItemStack returnedStack = ((IItemConduit)tile).insertItem(ForgeDirection.getOrientation(i).getOpposite(), itemstack, false);
                    if (returnedStack == null || returnedStack.stackSize == 0)
                        return;
                    else
                        itemstack = returnedStack;
                }
                else if (Loader.isModLoaded("BuildCraft|Transport") && tile instanceof IPipeTile)
                {
                    IPipeTile pipe = (IPipeTile)tile;
                    if (pipe.isPipeConnected(ForgeDirection.getOrientation(i).getOpposite()))
                    {
                        int size = pipe.injectItem(itemstack, true, ForgeDirection.getOrientation(i).getOpposite());
                        if (size >= itemstack.stackSize)
                            return;
                        else
                            itemstack.stackSize -= size;
                    }
                }
            }
        }
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, itemstack));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        insertDirection = compound.getByteArray("insertDirection");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByteArray("insertDirection", insertDirection);
    }
}
