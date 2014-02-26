package com.dynious.blex.tileentity;

import com.dynious.blex.api.IFilterTile;

public interface IAdvancedFilteredTile extends IAdvancedTile, IFilterTile
{
    public boolean getRestrictExtraction();

    public void setRestrictionExtraction(boolean restrict);

}
