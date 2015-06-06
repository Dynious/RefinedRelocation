package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

public class GuiEmptyFilter extends GuiWidgetBase
{

    public GuiEmptyFilter(int x, int y, int w, int h)
    {
        super(x, y, w, h);

        GuiLabel headerLabel = new GuiLabel(this, x, y + 50, StatCollector.translateToLocal(Strings.SELECT_FILTER_TYPE));
        headerLabel.drawCentered = false;

        new GuiFilterTypeList(this, x, y + h / 2 - 36, w, h);
    }

}
