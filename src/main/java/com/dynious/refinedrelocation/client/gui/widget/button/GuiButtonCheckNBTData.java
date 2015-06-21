package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.filter.SameItemFilter;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonCheckNBTData extends GuiButtonToggle
{
    private final int boundMessageId;
    protected SameItemFilter filter;

    public GuiButtonCheckNBTData(IGuiParent parent, int x, int y, SameItemFilter filter, int boundMessageId)
    {
        super(parent, x, y, 24, 20, 226, 80, null, null);
        this.boundMessageId = boundMessageId;
        this.filter = filter;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        filter.checkNBTData = newState;
        filter.getParentFilter().sendBooleanToServer(filter, boundMessageId, newState);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY))
        {
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.CHECK_NBT : Strings.DONT_CHECK_NBT));
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
        }
        return tooltip;
    }

    @Override
    public void update()
    {
        if (filter != null)
        {
            setState(filter.checkNBTData);
        }
        super.update();
    }
}
