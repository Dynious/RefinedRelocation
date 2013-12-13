package com.dynious.blex.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class TileAdvancedBlockExtender extends TileBlockExtender
{
    @Override
    public int[] getAccessibleSlotsFromSide(int i)
    {
        int[] slots = super.getAccessibleSlotsFromSide(i);
        int bestSlot = Integer.MAX_VALUE;
        for (int slot : slots)
        {
            ItemStack stack = getStackInSlot(slot);
            if (ItemStack.areItemStacksEqual())
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        return super.canInsertItem(i, itemStack, i2);
    }
}
