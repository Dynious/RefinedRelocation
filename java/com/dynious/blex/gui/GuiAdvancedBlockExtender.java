package com.dynious.blex.gui;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.TileAdvancedBlockExtender;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedBlockExtender extends GuiScreen
{
    private TileAdvancedBlockExtender blockExtender;
    private int currentTab = 0;
    private GuiButton spreadItems;

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
        this.buttonList.add(spreadItems = new GuiButton(0, width/2, height/2, blockExtender.spreadItems? "Spread on": "Spread off"));
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawContainerBackground();
    }
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        spreadItems.displayString = blockExtender.spreadItems? "Spread on": "Spread off";
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch(guibutton.id)
        {
            case 0:
                blockExtender.spreadItems = !blockExtender.spreadItems;
        }
    }


    private void drawContainerBackground()
    {
        int xSize = 176;
        int ySize = 166;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_ADVANCED_BLOCK_EXTENDER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
    }
}
