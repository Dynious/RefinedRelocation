package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonRedstoneToggle extends GuiButtonToggle
{
    protected TilePowerLimiter tile;

    public GuiButtonRedstoneToggle(IGuiParent parent, int x, int y, TilePowerLimiter tile)
    {
        super(parent, x, y, 24, 20, "button_redstone_high", "button_redstone_pulse", null, null);
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
        GuiHelper.sendBooleanMessage(MessageGUI.REDSTONE_TOGGLE, newState);
    }

    @Override
    public void getTooltip(List<String> tooltip, int mouseX, int mouseY) {
        super.getTooltip(tooltip, mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY))
        {
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.RS_PULSE : Strings.RS_ON));
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
        }
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
