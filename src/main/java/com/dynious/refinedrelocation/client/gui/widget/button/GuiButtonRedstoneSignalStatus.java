package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonRedstoneSignalStatus extends GuiButtonToggle
{
    protected TileBlockExtender tile;

    public GuiButtonRedstoneSignalStatus(IGuiParent parent, int x, int y, TileBlockExtender tile)
    {
        super(parent, x, y, 24, 20, "button_redstone_inactive", "button_redstone_active", null, null);
        this.tile = tile;
        update();
        setAdventureModeRestriction(true);
    }

    @Override
    public void getTooltip(List<String> tooltip, int mouseX, int mouseY) {
        super.getTooltip(tooltip, mouseX, mouseY);

        if (isInsideBounds(mouseX, mouseY))
        {
            tooltip.add(StatCollector.translateToLocal(Strings.REDSTONE_TRANSMISSION));
            String[] tooltipLines = StatCollector.translateToLocal(tile.isRedstoneTransmissionEnabled() ? Strings.REDSTONE_TRANSMISSION_ON_DESC : Strings.REDSTONE_TRANSMISSION_OFF_DESC).split("\\\\n");
            for (String tooltipLine : tooltipLines)
            {
                tooltip.add("\u00a77" + tooltipLine);
            }
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
        }
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
        {
            return;
        }

        tile.setRedstoneTransmissionEnabled(newState);
        GuiHelper.sendBooleanMessage(MessageGUI.REDSTONE_ENABLED, newState);
    }

    @Override
    public void update()
    {
        if (tile != null)
        {
            setState(tile.isRedstoneTransmissionEnabled());
        }

        super.update();
    }
}
