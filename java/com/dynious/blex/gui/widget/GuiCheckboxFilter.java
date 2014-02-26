package com.dynious.blex.gui.widget;

import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketFilterOption;
import com.dynious.blex.api.IFilterTile;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiCheckboxFilter extends GuiCheckbox
{
    protected IFilterTile tile;
    protected int index;

    public GuiCheckboxFilter(IGuiParent parent, int x, int y, int w, int h, int index, IFilterTile tile)
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
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketFilterOption((byte) index)));
    }

    @Override
    public void update()
    {
        if (tile != null)
            setChecked(tile.getFilter().getValue(index));

        super.update();
    }
}
