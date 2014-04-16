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

    /**
     * Should be called by the constructor of the Container of this tile, used to sync Inventory contents
     *
     * @param player The player opening the container
     */
    public void addCrafter(EntityPlayer player);

    /**
     * Should be called by onContainerClosed(...) of the Container of this this tile, used to stop syncing Inventory contents
     *
     * @param player The player closing the container
     */
    public void removeCrafter(EntityPlayer player);
}
