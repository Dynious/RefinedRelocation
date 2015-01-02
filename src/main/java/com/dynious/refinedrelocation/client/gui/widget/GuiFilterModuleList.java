package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.tileentity.INewFilterTile;
import com.dynious.refinedrelocation.client.gui.GuiModularFiltered;
import com.dynious.refinedrelocation.grid.FilterModuleRegistry;

public class GuiFilterModuleList extends GuiList
{
    private INewFilterTile tile;
    private int id;

    public GuiFilterModuleList(GuiModularFiltered parent, int x, int y, INewFilterTile tile, int id)
    {
        super(parent, x, y, 150, 100);
        this.tile = tile;
        this.id = id;
        init();
    }

    @Override
    public int getListSize()
    {
        return FilterModuleRegistry.getSize();
    }

    @Override
    public String getString(int index)
    {
        return index < FilterModuleRegistry.getSize() ? FilterModuleRegistry.getName(index) : "";
    }

    @Override
    public void onClicked(int index, boolean newState)
    {
        if (index < FilterModuleRegistry.getSize())
        {
            tile.getFilter().filters[id] = FilterModuleRegistry.getNew(index);
            ((GuiModularFiltered) parent).onModuleAdded(id);
            parent.removeChild(this);
        }
    }

    @Override
    public boolean getValue(int index)
    {
        return false;
    }
}
