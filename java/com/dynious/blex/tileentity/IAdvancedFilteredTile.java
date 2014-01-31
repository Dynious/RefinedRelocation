package com.dynious.blex.tileentity;

public interface IAdvancedFilteredTile extends IAdvancedTile, IFilterTile {

    public boolean getRestrictExtraction();
    
    public void setRestrictionExtraction( boolean restrict );
    
}
