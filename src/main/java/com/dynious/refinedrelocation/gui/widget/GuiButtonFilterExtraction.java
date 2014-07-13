package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageRestrictExtraction;
import com.dynious.refinedrelocation.tileentity.IAdvancedFilteredTile;
import net.minecraft.util.StatCollector;

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
        NetworkHandler.INSTANCE.sendToServer(new MessageRestrictExtraction(newState));
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.FILTERED_EXTRACT : Strings.UNFILTERED_EXTRACT));
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
