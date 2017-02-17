package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.client.gui.GuiRefinedRelocationContainer;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButton;
import com.dynious.refinedrelocation.grid.filter.CustomRegexFilter;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

public class GuiRegexFilter extends GuiWidgetBase implements IGuiWidgetWrapped
{
    protected CustomRegexFilter filter;
    protected boolean drawLabel = true;
    protected GuiLabel regexFilterLabel;
    protected GuiButton regexFilterHelp;
    protected GuiTextInputRegexFilter regexFilter;

    public GuiRegexFilter(int x, int y, int w, int h, boolean drawLabel, CustomRegexFilter filter)
    {
        super(x, y, w, h);
        this.drawLabel = drawLabel;
        this.filter = filter;

        regexFilterLabel = new GuiLabel(this, x, y, StatCollector.translateToLocal(Strings.REGEX_FILTER));
        regexFilterLabel.drawCentered = false;

        regexFilterHelp = new GuiButton(this, regexFilterLabel.x + regexFilterLabel.w + 5, regexFilterLabel.y - 1, 10, 10, "button_help", null);
        regexFilterHelp.setTooltipString(StatCollector.translateToLocal(Strings.NAME_MATCHING) + "\n\u00A77"
                + StatCollector.translateToLocal(Strings.REGEX_PATTERNS_VALID) + "\n\u00A77"
                + StatCollector.translateToLocal(Strings.OREDICT_CHARACTER) + ": \u00A72!\n\u00A77"
                + StatCollector.translateToLocal(Strings.NEWLINE_SEPARATION));

        regexFilter = new GuiTextInputRegexFilter(this, x, y + regexFilterLabel.h + 10, w, h - regexFilterLabel.h - 10, filter);
        regexFilter.setFocused(true);
        regexFilter.setEnabled(!GuiRefinedRelocationContainer.isRestrictedAccess());
    }
}
