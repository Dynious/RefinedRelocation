package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonRedstoneToggle extends GuiButtonToggle
{
    private final int boundMessageId;
    protected TilePowerLimiter tile;

    public GuiButtonRedstoneToggle(IGuiParent parent, int x, int y, TilePowerLimiter tile, int boundMessageId)
    {
        super(parent, x, y, 24, 20, "button_redstone_high", "button_redstone_pulse", null, null);
        this.boundMessageId = boundMessageId;
        this.tile = tile;
        update();
        setAdventureModeRestriction(true);
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.setRedstoneToggle(newState);
        GuiHelper.sendBooleanMessage(boundMessageId, newState);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY))
        {
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.RS_PULSE : Strings.RS_ON));
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
        }
        return tooltip;
    }

    @Override
    public void update()
    {
        if (tile != null)
        {
            setState(tile.getRedstoneToggle());
        }

        super.update();
    }
}
