package com.dynious.refinedrelocation.api.tileentity.grid;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class LocalizedStack
{
    public final ItemStack STACK;
    public final IInventory INVENTORY;
    public final int SLOT;

    public LocalizedStack(ItemStack stack, IInventory inventory, int slot)
    {
        this.STACK = stack;
        this.INVENTORY = inventory;
        this.SLOT = slot;
    }
}
