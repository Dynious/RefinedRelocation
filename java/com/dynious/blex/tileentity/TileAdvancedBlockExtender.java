package com.dynious.blex.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class TileAdvancedBlockExtender extends TileBlockExtender
{
    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (!super.canInsertItem(i, itemStack, i2))
        {
            return false;
        }
        int[] slots = super.getAccessibleSlotsFromSide(i);
        int bestSlot = Integer.MAX_VALUE;
        int bestSize = Integer.MAX_VALUE;
        for (int slot : slots)
        {
            ItemStack stack = getStackInSlot(slot);
            if (stack == null)
            {
                bestSlot = slot;
                break;
            }
            if (stack.stackSize < bestSize)
            {
                bestSlot = slot;
                bestSize = stack.stackSize;
                //STARTING AGAIN
            }
        }
        return i == bestSlot;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack)
    {
        if (!super.isItemValidForSlot(i, itemStack))
        {
            return false;
        }
        int[] slots = super.getAccessibleSlotsFromSide(i);
        int bestSlot = Integer.MAX_VALUE;
        int bestSize = Integer.MAX_VALUE;
        for (int slot : slots)
        {
            ItemStack stack = getStackInSlot(slot);
            if (stack == null)
            {
                bestSlot = slot;
                break;
            }
            if (stack.stackSize < bestSize)
            {
                bestSlot = slot;
                bestSize = stack.stackSize;
                //STARTING AGAIN
            }
        }
        return i == bestSlot;
    }
}
