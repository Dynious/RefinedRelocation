package com.dynious.blex.tileentity;

import com.google.common.primitives.Bytes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TileAdvancedBuffer extends TileBuffer implements IAdvancedTile
{
    private byte[] insertPrioritiesArrayProxy = {0, 1, 2, 3, 4, 5};
    private List<Byte> insertPriorities = new ArrayList<Byte>(Bytes.asList(insertPrioritiesArrayProxy));
    private boolean spreadItems = false;
    private byte nextInsertDirection;
    private int insertionTries = 0;

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
        int priority = insertPriorities.indexOf((byte) side);
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
    public int getNextInsertSide(int currentInsertSide)
    {
        if (spreadItems)
        {
            nextInsertDirection = (byte) Math.max(0, Math.min(nextInsertDirection, insertPriorities.size() - 1));
            if (insertionTries < insertPriorities.size())
            {
                insertionTries++;
                int side = insertPriorities.get(nextInsertDirection);
                if (nextInsertDirection < insertPriorities.size() - 1)
                    nextInsertDirection++;
                else
                    nextInsertDirection = 0;
                return side;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            if (currentInsertSide + 1 < insertPriorities.size())
            {
                return insertPriorities.get(currentInsertSide + 1);
            }
            else
            {
                return -1;
            }
        }
    }

    @Override
    public ItemStack outputItemStack(ItemStack itemstack, int inputSide)
    {
        if (spreadItems)
        {
            ItemStack tempStack = itemstack.copy();
            tempStack.stackSize = 1;

            int currentInsetSide = -1;
            while ((currentInsetSide = getNextInsertSide(currentInsetSide)) != -1)
            {
                if (currentInsetSide == inputSide)
                    continue;
                ItemStack returnedStack = insertItemStack(tempStack.copy(), currentInsetSide);
                if (returnedStack == null || returnedStack.stackSize == 0)
                {
                    itemstack.stackSize--;
                    insertionTries = 0;
                }
                if (itemstack == null || itemstack.stackSize == 0)
                    return null;
            }
        }
        else
        {
            int currentInsetSide = -1;
            while ((currentInsetSide = getNextInsertSide(currentInsetSide)) != -1)
            {
                if (currentInsetSide == inputSide)
                    continue;
                itemstack = insertItemStack(itemstack, currentInsetSide);
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
        insertPriorities.clear();
        byte byteArrayPriorities[] = compound.getByteArray("insertPriority");
        for (int priority = 0; priority < byteArrayPriorities.length; priority++)
        {
            setPriorityOfSideTo(byteArrayPriorities[priority], priority);
        }
        // any side not included in the saved data has a null priority
        for (byte i = 0; i < insertPrioritiesArrayProxy.length; i++)
        {
            if (!insertPriorities.contains(i))
                insertPrioritiesArrayProxy[i] = NULL_PRIORITY;
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
