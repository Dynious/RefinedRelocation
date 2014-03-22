package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.network.NetworkHelper;
import com.dynious.refinedrelocation.network.packet.PacketFilterOption;

public class GuiCheckboxFilter extends GuiCheckbox
{
    protected IFilterTileGUI tile;
    protected int index;

    public GuiCheckboxFilter(IGuiParent parent, int x, int y, int w, int h, int index, IFilterTileGUI tile)
    {
        super(parent, x, y, w, h, null);
        this.tile = tile;
        setIndex(index);
        update();
    }

    public void setIndex(int index)
    {
        this.index = index;
        if (tile != null)
            this.label.setText(tile.getFilter().getName(index));
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.getFilter().setValue(index, newState);
        NetworkHelper.sendToServer(new PacketFilterOption((byte) index));
    }

    @Override
    public void update()
    {
        if (tile != null)
            setChecked(tile.getFilter().getValue(index));

        super.update();
    }
}
