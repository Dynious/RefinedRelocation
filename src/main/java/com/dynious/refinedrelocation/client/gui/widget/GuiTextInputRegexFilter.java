package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.filter.CustomRegexFilter;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterString;

public class GuiTextInputRegexFilter extends GuiTextInputMultiline
{

    protected CustomRegexFilter filter;

    public GuiTextInputRegexFilter(IGuiParent parent, int x, int y, int w, int h, CustomRegexFilter filter)
    {
        super(parent, x, y, w, h);
        this.filter = filter;
        this.isMultiLine = true;

        update();
    }

    @Override
    protected void onTextChangedByUser(String newFilter)
    {
        filter.setValue(newFilter);
        filter.getParentFilter().sendStringToServer(filter, 0, newFilter);
    }

    @Override
    public void update()
    {
        if (!filter.getValue().equals(getText()))
        {
            setText(filter.getValue());
        }
        super.update();
    }

}
