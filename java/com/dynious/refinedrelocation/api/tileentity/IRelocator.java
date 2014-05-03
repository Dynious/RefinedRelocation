package com.dynious.refinedrelocation.api.tileentity;

import com.dynious.refinedrelocation.api.tileentity.handlers.IRelocatorHandler;
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

    public boolean isDecisionPoint();

    public TileEntity[] getConnectedInventories();

    public IRelocator[] getConnectedRelocators();

    public boolean passesFilter(ItemStack itemStack, int side);
}
