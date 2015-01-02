package com.dynious.refinedrelocation.api.tileentity;

import com.dynious.refinedrelocation.grid.Filter;
import net.minecraft.tileentity.TileEntity;

public interface INewFilterTile
{
    public Filter getFilter();

    public TileEntity getTileEntity();

    public void onFilterChanged();
}
