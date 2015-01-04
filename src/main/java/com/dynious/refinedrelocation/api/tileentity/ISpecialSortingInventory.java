package com.dynious.refinedrelocation.api.tileentity;

import com.dynious.refinedrelocation.api.tileentity.grid.SpecialLocalizedStack;

public interface ISpecialSortingInventory extends ISortingInventory
{
    // TODO Documentation
    public SpecialLocalizedStack getLocalizedStackInSlot(int slot);

    public void alterStackSize(int alteration);
}
