package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;

public interface IAdvancedFilteredTile extends IAdvancedTile, IFilterTileGUI
{
    public boolean getRestrictExtraction();

    public void setRestrictionExtraction(boolean restrict);

}
