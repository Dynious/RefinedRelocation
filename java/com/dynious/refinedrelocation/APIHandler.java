package com.dynious.refinedrelocation;

import com.dynious.refinedrelocation.api.IAPIHandler;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingMemberHandler;
import com.dynious.refinedrelocation.lib.GuiIds;
import com.dynious.refinedrelocation.sorting.FilterStandard;
import com.dynious.refinedrelocation.sorting.SortingInventoryHandler;
import com.dynious.refinedrelocation.sorting.SortingMemberHandler;
import net.minecraft.tileentity.TileEntity;

public class APIHandler implements IAPIHandler
{
    public static APIHandler instance = new APIHandler();

    public Object getModInstance()
    {
        return RefinedRelocation.instance;
    }

    public int getFilteringGUIID()
    {
        return GuiIds.FILTERED;
    }

    public IFilterGUI createStandardFilter()
    {
        return new FilterStandard();
    }

    public ISortingMemberHandler createSortingMemberHandler(TileEntity owner)
    {
        return new SortingMemberHandler(owner);
    }

    public ISortingInventoryHandler createSortingInventoryHandler(TileEntity owner)
    {
        return new SortingInventoryHandler(owner);
    }
}
