package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.IFilterGUITile;
import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.network.packet.PacketUserFilter;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiTextInputUserFilter extends GuiTextInput
{
    protected IFilterGUITile tile;

    public GuiTextInputUserFilter(IGuiParent parent, int x, int y, int w, int h, IFilterGUITile tile)
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
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketUserFilter(newFilter)));
    }

    @Override
    public void update()
    {
        if (tile != null)
            setText(tile.getFilter().getUserFilter());

        super.update();
    }

}
