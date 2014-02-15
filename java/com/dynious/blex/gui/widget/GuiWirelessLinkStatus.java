package com.dynious.blex.gui.widget;

import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.tileentity.TileWirelessBlockExtender;

import java.util.List;

public class GuiWirelessLinkStatus extends GuiBlExButton
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
            tooltip.add("Wireless Link");
            if (linked)
            {
                tooltip.add(grayColor + "Linked");
                tooltip.add(grayColor + "to: " + yellowColor + BlockHelper.getTileEntityDisplayName(tile.getConnectedTile()));
                tooltip.add(grayColor + "at: " + yellowColor + tile.xConnected + ":" + tile.yConnected + ":" + tile.zConnected);

                if (tile.hasConnection())
                {
                    tooltip.add(grayColor + "Connections:");
                    List<String> connections = tile.getConnectionTypes();
                    for (int i = 0; i < connections.size(); i++)
                        connections.set(i, yellowColor + connections.get(i));

                    tooltip.addAll(connections);
                }
            }
            else
            {
                tooltip.add(grayColor + "Unlinked");
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
