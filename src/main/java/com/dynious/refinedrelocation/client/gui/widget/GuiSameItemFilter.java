package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonCheckMetadata;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonCheckNBTData;
import com.dynious.refinedrelocation.grid.filter.SameItemFilter;
import net.minecraft.util.StatCollector;

public class GuiSameItemFilter extends GuiWidgetBase implements IGuiWidgetWrapped
{
    public GuiSameItemFilter(SameItemFilter filter, int x, int y, int w, int h)
    {
        super(x, y, w, h);

        GuiLabel headerLabel = new GuiLabel(this, x, y, StatCollector.translateToLocal(filter.getNameLangKey()));
        headerLabel.drawCentered = false;

        new GuiButtonCheckMetadata(this, x + 45, y + 20, filter, 0);
        new GuiButtonCheckNBTData(this, x + 95, y + 20, filter, 1);
    }
}
