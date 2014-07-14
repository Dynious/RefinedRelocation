package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageRedstoneEnabled;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiRedstoneSignalStatus extends GuiButtonToggle
{
    protected TileBlockExtender tile;
    protected boolean lastEnabled = false;
    protected boolean lastPowered = true;
    protected String tooltipText;
    protected static final int textureXBase = 128;

    public GuiRedstoneSignalStatus(IGuiParent parent, int x, int y, TileBlockExtender tile)
    {
        super(parent, x, y, 16, 16, textureXBase, 80, null, null);
        this.tile = tile;
        update();
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);

        if (isMouseInsideBounds(mouseX, mouseY))
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
            return;

        tile.setRedstoneTransmissionEnabled(newState);
        NetworkHandler.INSTANCE.sendToServer(new MessageRedstoneEnabled(newState));
    }

    @Override
    public void update()
    {
        super.update();

        if (tile != null)
        {
            setState(tile.isRedstoneTransmissionEnabled());
            this.textureX = tile.isRedstoneTransmissionActive() ? textureXBase + w : textureXBase;
        }
    }
}
