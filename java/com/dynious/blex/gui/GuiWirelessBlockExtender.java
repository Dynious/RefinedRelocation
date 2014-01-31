package com.dynious.blex.gui;

import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.tileentity.TileWirelessBlockExtender;

public class GuiWirelessBlockExtender extends GuiAdvancedFilteredBlockExtender
{
    private TileWirelessBlockExtender blockExtender;

    public GuiWirelessBlockExtender(TileWirelessBlockExtender blockExtender)
    {
        super(blockExtender);
        this.blockExtender = blockExtender;
    }

    @Override
    public void drawScreen(int h, int j, float f)
    {
        super.drawScreen(h, j, f);
        if (blockExtender.xConnected != Integer.MAX_VALUE)
        {
            String string = "Linked with " + BlockHelper.getTileEntityDisplayName(blockExtender.getConnectedTile()) + " at " + blockExtender.xConnected + ":" + blockExtender.yConnected + ":" + blockExtender.zConnected;
            fontRenderer.drawString(string, width / 2 - (fontRenderer.getStringWidth(string) / 2), height / 2 - 90, 0xFFFFFF);
        }
    }
}
