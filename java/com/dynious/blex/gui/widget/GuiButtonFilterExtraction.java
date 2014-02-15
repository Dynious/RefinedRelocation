package com.dynious.blex.gui.widget;

import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketRestrictExtraction;
import com.dynious.blex.tileentity.IAdvancedFilteredTile;
import cpw.mods.fml.common.network.PacketDispatcher;

import java.util.List;

public class GuiButtonFilterExtraction extends GuiButtonToggle
{
    protected IAdvancedFilteredTile tile;

    public GuiButtonFilterExtraction(IGuiParent parent, int x, int y, IAdvancedFilteredTile tile)
    {
        super(parent, x, y, 24, 20, 72, 0, null, null);
        this.tile = tile;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.setRestrictionExtraction(newState);
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketRestrictExtraction(newState)));
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
            tooltip.add(getState() ? "Filtered extraction" : "Unfiltered extraction");
        return tooltip;
    }

    @Override
    public void update()
    {
        if (tile != null)
            setState(tile.getRestrictExtraction());

        super.update();
    }
}
