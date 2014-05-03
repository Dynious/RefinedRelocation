package com.dynious.refinedrelocation.api.tileentity.handlers;

import com.dynious.refinedrelocation.api.tileentity.grid.IRelocatorGrid;

public interface IRelocatorHandler extends IGridMemberHandler
{
    /**
     * Get the Grid of this GridMember
     *
     * @return The Grid of this GridMember
     */
    public IRelocatorGrid getGrid();
}
