package com.dynious.refinedrelocation.api.tileentity.grid;


import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface ISortingGrid extends IGrid
{
    /**
     * Filters an ItemStack to all members of the SortingMember group
     *
     * @param itemStack The ItemStack to be filtered to all childs and this SortingMember
     * @return The ItemStack that was not able to fit in any ISortingInventory
     */
    public ItemStack filterStackToGroup(ItemStack itemStack, TileEntity requester, int slot);
}
