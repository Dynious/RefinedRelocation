package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.network.NetworkHelper;
import com.dynious.refinedrelocation.network.packet.PacketUserFilter;

public class GuiTextInputUserFilter extends GuiTextInput
{
    protected IFilterTileGUI tile;

    public GuiTextInputUserFilter(IGuiParent parent, int x, int y, int w, int h, IFilterTileGUI tile)
    {
        super(parent, x, y, w, h);
        this.tile = tile;
        update();
    }

    @Override
    protected void onTextChangedByUser(String newFilter)
    {
        if (tile == null)
            return;

        tile.getFilter().setUserFilter(newFilter);
        NetworkHelper.sendToServer(new PacketUserFilter(newFilter));
    }

    @Override
    public void update()
    {
        if (tile != null)
            setText(tile.getFilter().getUserFilter());

        super.update();
    }

}
