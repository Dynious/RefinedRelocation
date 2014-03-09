package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.IFilterTile;
import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.network.NetworkHelper;
import com.dynious.refinedrelocation.network.packet.PacketUserFilter;

public class GuiTextInputUserFilter extends GuiTextInput
{
    protected IFilterTile tile;

    public GuiTextInputUserFilter(IGuiParent parent, int x, int y, int w, int h, IFilterTile tile)
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

        tile.getFilter().userFilter = newFilter;
        NetworkHelper.sendToServer(new PacketUserFilter(newFilter));
    }

    @Override
    public void update()
    {
        if (tile != null)
            setText(tile.getFilter().userFilter);

        super.update();
    }

}
