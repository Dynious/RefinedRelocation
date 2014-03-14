package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.IFilterGUITile;

public interface IAdvancedFilteredTile extends IAdvancedTile, IFilterGUITile
{
    public boolean getRestrictExtraction();

    public void setRestrictionExtraction(boolean restrict);

}
