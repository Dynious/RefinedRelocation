package com.dynious.refinedrelocation.api;

/**
 * If your TileEntity implements the interface it will be able to open the Filtering GUI.
 *
 */
public interface IFilterTileGUI extends IFilterTile
{
    public IFilterGUI getFilter();
}
