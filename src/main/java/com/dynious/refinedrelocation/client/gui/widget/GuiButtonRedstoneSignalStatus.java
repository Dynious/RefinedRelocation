package com.dynious.refinedrelocation.client.gui.widget;

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
            String colorCode = "\u00A7";
            String grayColor = colorCode + "7";
            String redColor = colorCode + "4";

            tooltip.add(StatCollector.translateToLocal(Strings.REDSTONE_TRANSMISSION));
            if (tile.isRedstoneTransmissionEnabled())
            {
                tooltip.add(grayColor + StatCollector.translateToLocal(Strings.ENABLED));

                if (tile.isRedstoneTransmissionActive())
                    tooltip.add(redColor + StatCollector.translateToLocal(Strings.ACTIVE));
                else
                    tooltip.add(redColor + StatCollector.translateToLocal(Strings.INACTIVE));
            }
            else
            {
                tooltip.add(grayColor + StatCollector.translateToLocal(Strings.DISABLED));
            }
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
