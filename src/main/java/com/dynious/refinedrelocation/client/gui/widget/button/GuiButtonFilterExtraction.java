package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.IAdvancedFilteredTile;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonFilterExtraction extends GuiButtonToggle
{
    protected IAdvancedFilteredTile tile;

    public GuiButtonFilterExtraction(IGuiParent parent, int x, int y, IAdvancedFilteredTile tile)
    {
        super(parent, x, y, 24, 20, "button_unfiltered_extraction", "button_filtered_extraction", null, null);
        this.tile = tile;
        update();
        setAdventureModeRestriction(true);
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.setRestrictionExtraction(newState);
        GuiHelper.sendBooleanMessage(MessageGUI.FILTERED_EXTRACTION, newState);
    }

    @Override
    public void getTooltip(List<String> tooltip, int mouseX, int mouseY) {
        super.getTooltip(tooltip, mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY)) {
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.FILTERED_EXTRACT : Strings.UNFILTERED_EXTRACT));
            String[] tooltipLines = StatCollector.translateToLocal(getState() ? Strings.FILTERED_EXTRACT_DESC : Strings.UNFILTERED_EXTRACT_DESC).split("\\\\n");
            for (String tooltipLine : tooltipLines)
            {
                tooltip.add("\u00a77" + tooltipLine);
            }
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
        }
    }

    @Override
    public void update()
    {
        if (tile != null)
            setState(tile.getRestrictExtraction());

        super.update();
    }
}
