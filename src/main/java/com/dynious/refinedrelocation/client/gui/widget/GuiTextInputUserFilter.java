package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;

public class GuiTextInputUserFilter extends GuiTextInput
{
    private int boundMessageId;
    protected IFilterTileGUI tile;

    public GuiTextInputUserFilter(IGuiParent parent, int x, int y, int w, int h, IFilterTileGUI tile, int boundMessageId)
    {
        super(parent, x, y, w, h);
        this.tile = tile;
        this.boundMessageId = boundMessageId;
        setMaxStringLength(256);
        update();
    }

    @Override
    protected void onTextChangedByUser(String newFilter)
    {
        if (tile == null)
            return;

        tile.getFilter().setUserFilter(newFilter);
        GuiHelper.sendStringMessage(boundMessageId, newFilter);
    }

    @Override
    public void update()
    {
        if (tile != null && !tile.getFilter().getUserFilter().equals(textField.getText()))
            setText(tile.getFilter().getUserFilter());

        super.update();
    }
}
