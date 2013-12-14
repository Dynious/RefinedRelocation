package com.dynious.blex.gui;

import com.dynious.blex.tileentity.TileAdvancedBlockExtender;
import net.minecraft.client.gui.GuiScreen;

public class GuiAdvancedBlockExtender extends GuiScreen
{
    private TileAdvancedBlockExtender blockExtender;
    private int currentTab = 0;

    public GuiAdvancedBlockExtender(TileAdvancedBlockExtender blockExtender)
    {
        this.blockExtender = blockExtender;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
    }
}
