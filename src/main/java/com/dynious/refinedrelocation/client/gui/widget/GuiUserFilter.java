package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

public class GuiUserFilter extends GuiWidgetBase
{
    public int inputHeight = 15;
    protected IFilterTileGUI tile;
    protected boolean drawLabel = true;
    protected GuiLabel userFilterLabel;
    protected GuiButton userFilterHelp;
    protected GuiTextInputUserFilter userFilter;

    public GuiUserFilter(IGuiParent parent, int x, int y, int w, int h, boolean drawLabel, IFilterTileGUI tile, int boundMessageId)
    {
        super(parent, x, y, w, h);
        this.drawLabel = drawLabel;
        this.tile = tile;

        userFilterLabel = new GuiLabel(this, x, y, StatCollector.translateToLocal(Strings.CUSTOM_FILTER));
        userFilterLabel.drawCentered = false;

        userFilterHelp = new GuiButton(this, userFilterLabel.x + userFilterLabel.w + 5, userFilterLabel.y - 1, 10, 10, 0, 128, null);
        userFilterHelp.setTooltipString(StatCollector.translateToLocal(Strings.NAME_MATCHING) + "\n\u00A77"
                + StatCollector.translateToLocal(Strings.WILDCARD_CHARACTER) + ": \u00A73*\n\u00A77"
                + StatCollector.translateToLocal(Strings.OREDICT_CHARACTER) + ": \u00A72!\n\u00A77"
                + StatCollector.translateToLocal(Strings.COMMA_SEPARATION));

        userFilter = new GuiTextInputUserFilter(this, x, y + h - inputHeight, w, inputHeight, tile, boundMessageId);
    }
}
