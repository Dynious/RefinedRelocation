package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.network.packet.PacketFilterOption;
import com.dynious.refinedrelocation.network.packet.PacketLabeledFilterOption;
import com.dynious.refinedrelocation.sorting.FilterStandard;
import cpw.mods.fml.common.network.PacketDispatcher;

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
        if (index > FilterStandard.FILTER_SIZE)
        {
            PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketLabeledFilterOption(FilterStandard.getLabel(index))));
        }
        else
        {
            PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketFilterOption((byte) index)));
        }
    }

    @Override
    public void update()
    {
        if (tile != null)
            setChecked(tile.getFilter().getValue(index));

        super.update();
    }
}
