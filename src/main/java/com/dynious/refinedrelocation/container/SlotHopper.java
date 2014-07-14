package com.dynious.refinedrelocation.container;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class SlotHopper extends SlotFiltered
{
    public final ISidedInventory inventory;

    public SlotHopper(ISidedInventory inventory, int id, int x, int y)
    {
        super(inventory, id, x, y);
        this.inventory = inventory;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        return super.isItemValid(itemStack) && inventory.canInsertItem(this.getSlotIndex(), itemStack, -1);
    }
}
