package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonRedstoneSignalStatus extends GuiButtonToggle
{
    private final int boundMessageId;
    protected TileBlockExtender tile;

    public GuiButtonRedstoneSignalStatus(IGuiParent parent, int x, int y, TileBlockExtender tile, int boundMessageId)
    {
        super(parent, x, y, 24, 20, 202, 0, null, null);
        this.boundMessageId = boundMessageId;
        this.tile = tile;
        update();
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);

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

        return tooltip;
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
        {
            return;
        }

        tile.setRedstoneTransmissionEnabled(newState);
        GuiHelper.sendBooleanMessage(boundMessageId, newState);
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
