package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.grid.filter.MultiFilterRegistry;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

public class GuiEmptyFilter extends GuiWidgetBase {

    public GuiEmptyFilter(int x, int y, int w, int h) {
        super(x, y, w, h);

        new GuiLabel(this, x + w / 2, y + h / 2 - 20, StatCollector.translateToLocal(Strings.SELECT_FILTER_TYPE));

        int startX = x - 13 + w / 2 - 26;
        int endX = x - 13 + w / 2 + 26;
        int curX = startX;
        int curY = y + h / 2;
        for(Class<? extends IMultiFilterChild> entry : MultiFilterRegistry.getFilters()) {
            try {
                IMultiFilterChild filterChild = entry.newInstance();
                new GuiButtonFilterType(this, curX, curY, filterChild.getTypeName(), filterChild.getIconSheet(), filterChild.getIconX(), filterChild.getIconY());
                curX += 26;
                if(curX > endX) {
                    curX = startX;
                    curY += 26;
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
