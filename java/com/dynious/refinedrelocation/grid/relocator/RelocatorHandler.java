package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.grid.IGrid;
import com.dynious.refinedrelocation.api.tileentity.grid.IRelocatorGrid;
import com.dynious.refinedrelocation.api.tileentity.handlers.IRelocatorHandler;
import com.dynious.refinedrelocation.grid.GridMemberHandler;
import net.minecraft.tileentity.TileEntity;

public class RelocatorHandler extends GridMemberHandler implements IRelocatorHandler
{
    public RelocatorHandler(TileEntity owner)
    {
        super(owner);
    }

    public IRelocatorGrid getGrid()
    {
        return (IRelocatorGrid) grid;
    }

    /**
     * Creates a new Grid
     *
     * @return A new Grid
     */
    protected IGrid createNewGrid()
    {
        return new RelocatorGrid();
    }
}
