package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.IFilterTile;

public interface IAdvancedFilteredTile extends IAdvancedTile, IFilterTile
{
    public boolean getRestrictExtraction();

    public void setRestrictionExtraction(boolean restrict);

}
