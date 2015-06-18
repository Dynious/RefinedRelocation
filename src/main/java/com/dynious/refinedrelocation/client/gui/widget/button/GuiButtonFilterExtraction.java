package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.IAdvancedFilteredTile;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonFilterExtraction extends GuiButtonToggle
{
    private final int boundMessageId;
    protected IAdvancedFilteredTile tile;

    public GuiButtonFilterExtraction(IGuiParent parent, int x, int y, IAdvancedFilteredTile tile, int boundMessageId)
    {
        super(parent, x, y, 24, 20, 72, 0, null, null);
        this.tile = tile;
        this.boundMessageId = boundMessageId;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.setRestrictionExtraction(newState);
        GuiHelper.sendBooleanMessage(boundMessageId, newState);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY)) {
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.FILTERED_EXTRACT : Strings.UNFILTERED_EXTRACT));
            String[] tooltipLines = StatCollector.translateToLocal(getState() ? Strings.FILTERED_EXTRACT_DESC : Strings.UNFILTERED_EXTRACT_DESC).split("\\\\n");
            for (String tooltipLine : tooltipLines)
            {
                tooltip.add("\u00a77" + tooltipLine);
            }
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
        }
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
