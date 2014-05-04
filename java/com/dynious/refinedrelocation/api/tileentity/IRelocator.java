package com.dynious.refinedrelocation.api.tileentity;

import com.dynious.refinedrelocation.api.tileentity.handlers.IRelocatorHandler;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface IRelocator extends IGridMember
{
    /**
     * This should return the IRelocatorHandlerHandler of this tile. It cannot be null.
     * Create a new RelocatorHandle instance using APIUtils.createRelocatorHandler(...)
     *
     * @return The RelocatorHandler of this tile
     */
    public IRelocatorHandler getHandler();

    public TileEntity[] getConnectedInventories();

    public IRelocator[] getConnectedRelocators();

    public boolean passesFilter(ItemStack itemStack, int side);

    public ItemStack insert(ItemStack itemStack, int side, boolean simulate);

    public void receiveTravellingItem(TravellingItem item, int side);
}
