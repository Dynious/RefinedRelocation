package com.dynious.refinedrelocation.api.tileentity.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ISortingInventoryHandler extends ISortingMemberHandler
{
    /**
     * Used when adding an item to the inventory of the TileEntity
     * (Should be called by setInventorySlotContents(...) in the TileEntity class)
     */
    public void setInventorySlotContents(int par1, ItemStack itemStack);
}
