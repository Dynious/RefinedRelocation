package com.dynious.blex.tileentity;

import buildcraft.api.transport.IPipeTile;
import cofh.api.transport.IItemConduit;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraftforge.common.ForgeDirection;

public class TileAdvancedBuffer extends TileBuffer implements IAdvancedTile
{
    private byte[] insertDirection = {0, 1, 2, 3, 4, 5};
    private boolean spreadItems = false;
    private byte nextInsertDirection;

    public byte[] getInsertDirection()
    {
        return insertDirection;
    }

    public void setInsertDirection(int from, int value)
    {
        if (value > ForgeDirection.VALID_DIRECTIONS.length)
            value = 0;
        insertDirection[from] = (byte) value;
    }

    public String getInsertionName(int place)
    {
        if (getInsertDirection()[place] >= ForgeDirection.VALID_DIRECTIONS.length)
            return "NONE";
        return ForgeDirection.getOrientation(getInsertDirection()[place]).toString();
    }

    @Override
    public byte getMaxStackSize()
    {
        return 0;
    }

    @Override
    public void setMaxStackSize(byte maxStackSize)
    {

    }

    @Override
    public boolean getSpreadItems()
    {
        return spreadItems;
    }

    @Override
    public void setSpreadItems(boolean spreadItems)
    {
        this.spreadItems = spreadItems;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack)
    {
        if (spreadItems)
        {
            ItemStack tempStack = itemstack.copy();
            tempStack.stackSize = 1;
            int tries = 0;
            while (tries < 6)
            {
                tries++;
                int side = insertDirection[nextInsertDirection];
                if (nextInsertDirection < insertDirection.length - 1)
                    nextInsertDirection++;
                else
                    nextInsertDirection = 0;

                if (side >= ForgeDirection.VALID_DIRECTIONS.length || side >= tiles.length || side == slot)
                    continue;
                ItemStack returnedStack = insertItemStack(tempStack.copy(), side);
                if (returnedStack == null || returnedStack.stackSize == 0)
                {
                    itemstack.stackSize--;
                    tries = 0;
                }
                if (itemstack == null || itemstack.stackSize == 0)
                    return;
            }
        }
        else
        {
            for (int i : insertDirection)
            {
                if (i == 6 || i >= tiles.length || i == slot)
                    continue;
                itemstack = insertItemStack(itemstack, i);
                if (itemstack == null || itemstack.stackSize == 0)
                    return;
            }
        }
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, itemstack));
    }

    public ItemStack insertItemStack(ItemStack itemstack, int side)
    {
        TileEntity tile = tiles[side];
        if (tile != null)
        {
            if (Loader.isModLoaded("CoFHCore") && tile instanceof IItemConduit)
            {
                return ((IItemConduit) tile).insertItem(ForgeDirection.getOrientation(side).getOpposite(), itemstack);
            }
            else if (Loader.isModLoaded("BuildCraft|Transport") && tile instanceof IPipeTile)
            {
                IPipeTile pipe = (IPipeTile) tile;
                if (pipe.isPipeConnected(ForgeDirection.getOrientation(side).getOpposite()))
                {
                    int size = pipe.injectItem(itemstack, true, ForgeDirection.getOrientation(side).getOpposite());
                    itemstack.stackSize -= size;
                    return itemstack;
                }
            }
            else if (tile instanceof IInventory)
            {
                return TileEntityHopper.insertStack((IInventory) tile, itemstack, ForgeDirection.OPPOSITES[side]);
            }
        }
        return itemstack;
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
