package com.dynious.refinedrelocation.api.tileentity;

import com.dynious.refinedrelocation.api.tileentity.grid.LocalizedStack;

public interface ISpecialSortingInventory extends ISortingInventory
{

    // TODO Documentation
    public LocalizedStack getLocalizedStackInSlot(int slot);
}
