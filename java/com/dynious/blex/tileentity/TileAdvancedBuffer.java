package com.dynious.blex.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TileAdvancedBuffer extends TileBuffer implements IAdvancedTile
{
    private byte[] insertPrioritiesArrayProxy = {NULL_PRIORITY, NULL_PRIORITY, NULL_PRIORITY, NULL_PRIORITY, NULL_PRIORITY, NULL_PRIORITY};
    private List<Byte> insertPriorities = new ArrayList<Byte>();
    private boolean spreadItems = false;
    private byte nextInsertDirection;

    public static final byte NULL_PRIORITY = (byte) (ForgeDirection.VALID_DIRECTIONS.length);

    public byte[] getInsertDirection()
    {
        return insertPrioritiesArrayProxy;
    }

    public void setInsertDirection(int from, int value)
    {
        setPriorityOfSideTo(from, value);
    }

    public void setPriorityOfSideTo(int side, int priority)
    {
        priority = Math.min(NULL_PRIORITY, Math.max(0, priority));
        if (getPriority(side) < priority && priority == insertPriorities.size())
            priority = NULL_PRIORITY;

        insertPriorities.remove(new Byte((byte) side));
        if (priority != NULL_PRIORITY)
            insertPriorities.add(Math.min(insertPriorities.size(), priority), (byte) side);

        for (int i = 0; i < insertPrioritiesArrayProxy.length; i++)
        {
            insertPrioritiesArrayProxy[i] = getPriority(i);
        }
    }

    public byte getPriority(int side)
    {
        int priority = insertPriorities.indexOf(new Byte((byte) side));
        if (priority == -1)
            priority = NULL_PRIORITY;
        return (byte) priority;
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
    public ItemStack outputItemStack(ItemStack itemstack, int inputSide)
    {
        if (spreadItems)
        {
            ItemStack tempStack = itemstack.copy();
            tempStack.stackSize = 1;
            int tries = 0;
            nextInsertDirection = (byte) Math.max(0, Math.min(nextInsertDirection, insertPriorities.size() - 1));
            while (tries < 6)
            {
                tries++;
                int side = insertPriorities.get(nextInsertDirection);
                if (nextInsertDirection < insertPriorities.size() - 1)
                    nextInsertDirection++;
                else
                    nextInsertDirection = 0;

                if (side >= tiles.length || side == inputSide)
                    continue;
                ItemStack returnedStack = insertItemStack(tempStack.copy(), side);
                if (returnedStack == null || returnedStack.stackSize == 0)
                {
                    itemstack.stackSize--;
                    tries = 0;
                }
                if (itemstack == null || itemstack.stackSize == 0)
                    return null;
            }
        }
        else
        {
            for (int side : insertPriorities)
            {
                if (side >= tiles.length || side == inputSide)
                    continue;
                itemstack = insertItemStack(itemstack, side);
                if (itemstack == null || itemstack.stackSize == 0)
                    return null;
            }
        }
        return itemstack;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        byte byteArrayPriorities[] = compound.getByteArray("insertPriority");
        for (int priority = 0; priority < byteArrayPriorities.length; priority++)
        {
            setPriorityOfSideTo(byteArrayPriorities[priority], priority);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        byte byteArrayPriorities[] = new byte[insertPriorities.size()];
        for (int priority = 0; priority < insertPriorities.size(); priority++)
        {
            byteArrayPriorities[priority] = insertPriorities.get(priority);
        }
        compound.setByteArray("insertPriority", byteArrayPriorities);
    }
}
