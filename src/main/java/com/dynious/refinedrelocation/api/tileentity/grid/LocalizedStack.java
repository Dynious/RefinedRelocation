package com.dynious.refinedrelocation.api.tileentity.grid;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class LocalizedStack
{
    public final ItemStack STACK;
    public final IInventory INVENTORY;
    public final int SLOT;
    public final int SIZE;

    public LocalizedStack(ItemStack stack, IInventory inventory, int slot)
    {
        this.STACK = stack;
        this.INVENTORY = inventory;
        this.SLOT = slot;
        this.SIZE = stack.stackSize;
    }

    public LocalizedStack(ItemStack stack, IInventory inventory, int slot, int size)
    {
        this.STACK = stack;
        this.INVENTORY = inventory;
        this.SLOT = slot;
        this.SIZE = size;
    }
}
