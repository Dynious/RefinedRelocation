package com.dynious.blex.gui;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.TileFilteredBlockExtender;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

public class GuiFilteredBlockExtender extends GuiScreen
{
    private TileFilteredBlockExtender blockExtender;

    public GuiFilteredBlockExtender(TileFilteredBlockExtender blockExtender)
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

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int h, int j, float f)
    {
        drawDefaultBackground();
        drawContainerBackground();
        super.drawScreen(h, j, f);
    }
    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch(guibutton.id)
        {
            case 0:

        }
    }

    @Override
    protected void mouseClicked(int x, int y, int type)
    {
        super.mouseClicked(x, y, type);
    }

    private void drawContainerBackground()
    {
        int xSize = 176;
        int ySize = 80;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_ADVANCED_BLOCK_EXTENDER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
