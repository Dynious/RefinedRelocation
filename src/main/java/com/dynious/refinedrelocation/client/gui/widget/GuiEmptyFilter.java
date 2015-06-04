package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.client.gui.GuiFiltered;
import com.dynious.refinedrelocation.grid.filter.MultiFilterRegistry;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

public class GuiEmptyFilter extends GuiWidgetBase {

    public GuiEmptyFilter(GuiFiltered parentGui, int x, int y, int w, int h, IMultiFilter filter) {
        super(x, y, w, h);

        new GuiLabel(this, x + w / 2, y + 20, StatCollector.translateToLocal(Strings.SELECT_FILTER_TYPE));

        int startX = x - 12 + w / 2 - 25;
        int endX = x - 12 + w / 2 + 25;
        int curX = x - 12 + w / 2 - 25;
        int curY = y + 30;
        for(Class<? extends IMultiFilterChild> entry : MultiFilterRegistry.getFilters()) {
            try {
                IMultiFilterChild filterChild = entry.newInstance();
                new GuiButtonFilterType(this, curX, curY, filterChild.getTypeName(), filterChild.getIconSheet(), filterChild.getIconX(), filterChild.getIconY());
                curX += 25;
                if(curX > endX) {
                    curX = startX;
                    curY += 25;
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
