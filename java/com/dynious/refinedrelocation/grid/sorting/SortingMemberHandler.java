package com.dynious.refinedrelocation.grid.sorting;

import com.dynious.refinedrelocation.api.tileentity.grid.IGrid;
import com.dynious.refinedrelocation.api.tileentity.grid.ISortingGrid;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingMemberHandler;
import com.dynious.refinedrelocation.grid.GridMemberHandler;
import net.minecraft.tileentity.TileEntity;

public class SortingMemberHandler extends GridMemberHandler implements ISortingMemberHandler
{
    public SortingMemberHandler(TileEntity owner)
    {
        super(owner);
    }

    public ISortingGrid getGrid()
    {
        return (ISortingGrid) grid;
    }

    /**
     * Creates a new Grid
     *
     * @return A new Grid
     */
    protected IGrid createNewGrid()
    {
        return new SortingGrid();
    }
}
