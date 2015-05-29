package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;

public interface IAdvancedFilteredTile extends IAdvancedTile, IFilterTileGUI
{
    boolean getRestrictExtraction();

    void setRestrictionExtraction(boolean restrict);

}
