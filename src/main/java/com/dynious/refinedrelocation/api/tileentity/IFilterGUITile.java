package com.dynious.refinedrelocation.api.tileentity;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import net.minecraft.tileentity.TileEntity;

/**
 * If your TileEntity implements the interface it will be able to open the Filtering GUI.
 * @deprecated This interface will be renamed to IMultiFilterTile during the update to Minecraft 1.8. We already provide a dummy interface named like that which currently just extends this, so consider using that instead.
 */
@Deprecated
public interface IFilterGUITile extends IFilterTile
{
    IFilterGUI getFilter();

    TileEntity getTileEntity();

    void onFilterChanged();
}
