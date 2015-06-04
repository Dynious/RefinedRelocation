package com.dynious.refinedrelocation.api.tileentity;

import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import net.minecraft.tileentity.TileEntity;

/**
 * If your TileEntity implements the interface it will be able to open the Filtering GUI.
 */
public interface IMultiFilterTile extends IFilterTile
{
    IMultiFilter getFilter();

    TileEntity getTileEntity();

    void onFilterChanged();
}
