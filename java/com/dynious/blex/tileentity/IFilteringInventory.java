package com.dynious.blex.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IFilteringInventory extends IFilteringMember, IFilterTile, IInventory
{
    public ItemStack putInInventory(ItemStack itemStack);
}
