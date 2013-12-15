package com.dynious.blex.gui;

import com.dynious.blex.tileentity.TileAdvancedFilteredBlockExtender;
import com.dynious.blex.tileentity.TileWirelessBlockExtender;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class GuiWirelessBlockExtender extends GuiAdvancedFilteredBlockExtender
{
    private TileWirelessBlockExtender blockExtender;

    public GuiWirelessBlockExtender(TileWirelessBlockExtender blockExtender)
    {
        super(blockExtender);
        this.blockExtender = blockExtender;
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    public void drawScreen(int h, int j, float f)
    {
        super.drawScreen(h, j, f);
        String string = "Linked with TileEntity at: " + blockExtender.xConnected + ":" + blockExtender.yConnected + ":" + blockExtender.zConnected;
        fontRenderer.drawString(string, width/2 - (fontRenderer.getStringWidth(string)/2), height/2 - 90, 0xFFFFFF);
    }
}
