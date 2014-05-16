package com.dynious.refinedrelocation.api;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IRelocatorFilter;
import com.dynious.refinedrelocation.api.tileentity.handlers.IGridMemberHandler;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingMemberHandler;
import net.minecraft.tileentity.TileEntity;

public interface IAPIHandler
{
    public Object getModInstance();

    public int getFilteringGUIID();

    public IFilterGUI createStandardFilter();

    public ISortingMemberHandler createSortingMemberHandler(TileEntity owner);

    public ISortingInventoryHandler createSortingInventoryHandler(TileEntity owner);

    public void registerRelocatorFilter(String identifier, Class<? extends IRelocatorFilter> clazz) throws IllegalArgumentException;
}
