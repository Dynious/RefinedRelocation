package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.gui.IGuiParent;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.network.packet.PacketFilterOption;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.Minecraft;

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
            this.label.setText(Minecraft.getMinecraft().fontRenderer.trimStringToWidth(tile.getFilter().getName(index), w - (textureW + 6)));
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.getFilter().setValue(index, newState);
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketFilterOption(index)));
    }

    @Override
    public void update()
    {
        if (tile != null)
            setChecked(tile.getFilter().getValue(index));

        super.update();
    }
}
