package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.FilterCreativeTabs;

public class GuiCreativeTabsList extends GuiList
{
    private FilterCreativeTabs filter;

    public GuiCreativeTabsList(IGuiParent parent, int x, int y, int w, int h, FilterCreativeTabs filter)
    {
        super(parent, x, y, w, h);
        this.filter = filter;
        init();
    }

    @Override
    public int getListSize()
    {
        return filter.getSize();
    }

    @Override
    public String getString(int index)
    {
        return filter.getName(index);
    }

    @Override
    public void onClicked(int index, boolean newState)
    {
        //TODO: sync
    }

    @Override
    public boolean getValue(int index)
    {
        return filter.getValue(index);
    }
}
