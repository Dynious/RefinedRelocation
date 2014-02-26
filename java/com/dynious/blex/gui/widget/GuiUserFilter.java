package com.dynious.blex.gui.widget;

import com.dynious.blex.api.IFilterTile;
import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.lib.Strings;
import net.minecraft.util.StatCollector;

public class GuiUserFilter extends GuiBlExWidgetBase
{
    protected IFilterTile tile;
    protected boolean drawLabel = true;

    protected GuiLabel userFilterLabel;
    protected GuiBlExButton userFilterHelp;
    protected GuiTextInputUserFilter userFilter;

    public int inputHeight = 15;

    public GuiUserFilter(IGuiParent parent, int x, int y, int w, int h, boolean drawLabel, IFilterTile tile)
    {
        super(parent, x, y, w, h);
        this.drawLabel = drawLabel;
        this.tile = tile;

        userFilterLabel = new GuiLabel(this, x, y, StatCollector.translateToLocal(Strings.CUSTOM_FILTER));
        userFilterLabel.drawCentered = false;

        userFilterHelp = new GuiBlExButton(this, userFilterLabel.x + userFilterLabel.w + 5, userFilterLabel.y - 1, 10, 10, 0, 128, null);
        userFilterHelp.setTooltipString(StatCollector.translateToLocal(Strings.NAME_MATCHING) + "\n\u00A77"
                + StatCollector.translateToLocal(Strings.WILDCARD_CHARACTER) +  ": \u00A73*\n\u00A77"
                + StatCollector.translateToLocal(Strings.OREDICT_CHARACTER) + ": \u00A72!\n\u00A77"
                + StatCollector.translateToLocal(Strings.COMMA_SEPARATION));

        userFilter = new GuiTextInputUserFilter(this, x, y + h - inputHeight, w, inputHeight, tile);
    }
}
