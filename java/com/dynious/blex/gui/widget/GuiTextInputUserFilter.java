package com.dynious.blex.gui.widget;

import com.dynious.blex.api.IFilterTile;
import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketUserFilter;
import cpw.mods.fml.common.network.PacketDispatcher;

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
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketUserFilter(newFilter)));
    }

    @Override
    public void update()
    {
        if (tile != null)
            setText(tile.getFilter().userFilter);

        super.update();
    }

}
