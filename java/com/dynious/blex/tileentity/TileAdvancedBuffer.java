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
    private byte[] insertPriority = {0, 1, 2, 3, 4, 5};
    private boolean spreadItems = false;
    private byte nextInsertDirection;

    public static final byte NULL = 6;

    public byte[] getInsertDirection()
    {
        return insertPriority;
    }

    public void setInsertDirection(int from, int value)
    {
        byte priority = getPriority(from);
        System.out.println(from + ":" + priority + ":" +  value);
        if (priority != NULL)
        {
            insertPriority[priority] = NULL;
        }
        if (value != NULL)
        {
            insertPriority[value] = (byte) from;
        }
    }

    public byte getPriority(int side)
    {
        for (byte b : insertPriority)
        {
            if (b == side)
            {
                return b;
            }
        }
        return NULL;
    }

    public byte getNextInsertPriority(byte currentPriority, boolean reverse)
    {
        if (!reverse)
        {
            for (int i = currentPriority; i < insertPriority.length; i++)
            {
                byte b = insertPriority[i];
                if (b == NULL)
                {
                    return (byte) i;
                }
            }
            for (int i = 0; i < currentPriority; i++)
            {
                byte b = insertPriority[i];
                if (b == NULL)
                {
                    return (byte) i;
                }
            }
        }
        else
        {
            for (int i = Math.min(5, currentPriority); i > -1; i--)
            {
                byte b = insertPriority[i];
                if (b == NULL)
                {
                    return (byte) i;
                }
            }
            for (int i = insertPriority.length - 1; i > currentPriority; i--)
            {
                byte b = insertPriority[i];
                if (b == NULL)
                {
                    return (byte) i;
                }
            }
        }
        return NULL;
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
                int side = insertPriority[nextInsertDirection];
                if (nextInsertDirection < insertPriority.length - 1)
                    nextInsertDirection++;
                else
                    nextInsertDirection = 0;

                if (side == NULL || side >= tiles.length || side == slot)
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
            for (int i : insertPriority)
            {
                if (i == NULL || i >= tiles.length || i == slot)
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
        insertPriority = compound.getByteArray("insertPriority");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByteArray("insertPriority", insertPriority);
    }
}
