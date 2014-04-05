package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiWirelessLinkStatus extends GuiRefinedRelocationButton
{
    public static final int linkedOffsetY = 80;
    public static final int unlinkedOffsetY = 112;

    protected TileWirelessBlockExtender tile;
    protected boolean linked;
    protected boolean lastLinked;
    protected String tooltipText;

    public GuiWirelessLinkStatus(IGuiParent parent, int x, int y, TileWirelessBlockExtender tile)
    {
        super(parent, x, y, 16, 16, 112, unlinkedOffsetY, null);
        this.tile = tile;
        update();
    }

    public void setLinked(boolean state)
    {
        this.linked = state;

        this.textureY = linked ? linkedOffsetY : unlinkedOffsetY;
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);

        if (isMouseInsideBounds(mouseX, mouseY))
        {
            String colorCode = "\u00A7";
            String grayColor = colorCode + "7";
            String yellowColor = colorCode + "e";
            tooltip.add(StatCollector.translateToLocal(Strings.WIRELESS_LINK));
            if (linked)
            {
                tooltip.add(grayColor + StatCollector.translateToLocalFormatted(Strings.LINKED_TO_AT, BlockHelper.getBlockDisplayName(tile.worldObj, tile.xConnected, tile.yConnected, tile.zConnected), tile.xConnected, tile.yConnected, tile.zConnected));

                if (tile.hasConnection())
                {
                    tooltip.add(grayColor + StatCollector.translateToLocal(Strings.CONNECTIONS) + ":");
                    List<String> connections = tile.getConnectionTypes();
                    for (int i = 0; i < connections.size(); i++)
                        connections.set(i, yellowColor + connections.get(i));

                    tooltip.addAll(connections);
                }
            }
            else
            {
                tooltip.add(grayColor + StatCollector.translateToLocal(Strings.UNLINKED));
            }
        }

        return tooltip;
    }

    @Override
    public void update()
    {
        super.update();

        boolean isLinked = tile.xConnected != Integer.MAX_VALUE;
        if (lastLinked != isLinked)
            setLinked(isLinked);
    }
}
