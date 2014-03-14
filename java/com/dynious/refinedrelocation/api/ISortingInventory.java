package com.dynious.refinedrelocation.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * This extends the {@link ISortingMember} interface.
 * Tile that implement this interface will be part of the Filtering Network
 * and their inventory will actively be used in the network.
 * The FilterStandard set in the IFilterGUITile interface will be used as the filter.
 *
 * Make sure you call all required methods of {@link ISortingMember} as well as
 * setInventorySlotContents(...) when this is called in your tile, addCrafter(...)
 * when a player opens the container of this tile (in constructor of {@link net.minecraft.inventory.Container})
 * and removeCrafter(...) when a container is closed (onContainerClosed(...) in {@link net.minecraft.inventory.Container}).
 * You also need to override putStackInSlot(...) in the container of your TileEntity and call putStackInSlot(...).
 */
public interface ISortingInventory extends ISortingMember, IInventory, IFilterTile
{
    /**
     * This should return the SortingInventoryHandler of this tile. It cannot be null.
     *
     * @return The SortingInventoryHandler of this tile
     */
    public SortingInventoryHandler getSortingHandler();

    /**
     * Should return all stored ItemStacks in this tile.
     *
     * @return The stored ItemStacks
     */
    public ItemStack[] getInventory();
}
