package com.dynious.blex.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class TileAdvancedBlockExtender extends TileBlockExtender
{
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack)
    {
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
            if (ItemStack.areItemStacksEqual(stack, itemStack) && stack.stackSize + itemStack.stackSize <= stack.getMaxStackSize())
            {
                if (stack.stackSize < bestSize)
                {
                    bestSlot = slot;
                    bestSize = stack.stackSize;
                    //STARTING AGAIN
                }
            }
        }
        super.setInventorySlotContents(bestSlot, itemStack);
    }
}
