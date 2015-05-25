package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.client.gui.GuiFiltered;
import com.dynious.refinedrelocation.grid.filter.AbstractFilter;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

public class GuiEmptyFilter extends GuiWidgetBase {

    public GuiEmptyFilter(GuiFiltered parentGui, int x, int y, int w, int h, IFilterGUI filter) {
        super(x, y, w, h);

        new GuiLabel(this, x + w / 2, y, StatCollector.translateToLocal(Strings.SELECT_FILTER_TYPE));

        // TODO buttons should get icons instead of letters obviously
        new GuiButtonFilterType(this, x - 12 + w / 2 - 25 , y + 15, "C", parentGui, filter, AbstractFilter.TYPE_CREATIVETAB);
        new GuiButtonFilterType(this, x - 12 + w / 2, y + 15,  "P", parentGui, filter, AbstractFilter.TYPE_PRESET);
        new GuiButtonFilterType(this, x - 12 + w / 2 + 25, y + 15, "U", parentGui, filter, AbstractFilter.TYPE_CUSTOM);
    }

}
