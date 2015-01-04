package com.dynious.refinedrelocation.api.filter;

import com.dynious.refinedrelocation.api.tileentity.INewFilterTile;

public abstract class FilterModuleBase implements IFilterModule
{
    protected INewFilterTile filterTile;

    @Override
    public void init(INewFilterTile filterTile)
    {
        this.filterTile = filterTile;
    }
}
