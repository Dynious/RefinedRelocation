package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonRedstoneToggle extends GuiButtonToggle
{
    protected TilePowerLimiter tile;

    public GuiButtonRedstoneToggle(IGuiParent parent, int x, int y, TilePowerLimiter tile)
    {
        super(parent, x, y, 24, 20, 96, 0, null, null);
        this.tile = tile;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.setRedstoneToggle(newState);
        GuiHelper.sendBooleanMessage(0, newState);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.RS_PULSE : Strings.RS_ON));
        return tooltip;
    }

    @Override
    public void update()
    {
        if (tile != null)
            setState(tile.getRedstoneToggle());

        super.update();
    }
}
