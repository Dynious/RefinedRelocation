package com.dynious.blex.api;

import com.dynious.blex.tileentity.IFilterTile;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * This extends the {@link IFilteringMember} interface.
 * Tile that implement this interface will be part of the Filtering Network
 * and their inventory will actively be used in the network.
 * The Filter set in the IFilterTile interface will be used as the filter.
 *
 * Make sure you call all required methods of {@link IFilteringMember} as well as
 * setInventorySlotContents(...) when this is called in your tile, addCrafter(...)
 * when a player opens the container of this tile (in constructor of {@link net.minecraft.inventory.Container})
 * and removeCrafter(...) when a container is closed (onContainerClosed(...) in {@link net.minecraft.inventory.Container}).
 * You also need to override putStackInSlot(...) in the container of your TileEntity and call putStackInSlot(...).
 */
public interface IFilteringInventory extends IFilteringMember, IInventory, IFilterTile
{
    /**
     * This should return the FilteringInventoryHandler of this tile. It cannot be null.
     * This should be the same as the getFilteringMemberHandler() return.
     *
     * @return The FilteringInventoryHandler of this tile
     */
    public FilteringInventoryHandler getFilteringInventoryHandler();

    /**
     * Should return all stored ItemStacks in this tile.
     *
     * @return The stored ItemStacks
     */
    public ItemStack[] getInventory();
}
