package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.tileentity.IMultiFilterTile;

public interface IAdvancedFilteredTile extends IAdvancedTile, IMultiFilterTile
{
    boolean getRestrictExtraction();

    void setRestrictionExtraction(boolean restrict);

}
