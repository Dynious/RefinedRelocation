package com.dynious.blex.gui;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.TileFilteredBlockExtender;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiFilteredBlockExtender extends GuiScreen
{
    private TileFilteredBlockExtender blockExtender;
    private GuiButton blacklist;
    private int index = 0;
    private int size;
    private static final int ITEMS_PER_SCREEN = 10;
    private static final int ITEM_SIZE = 14;

    public GuiFilteredBlockExtender(TileFilteredBlockExtender blockExtender)
    {
        this.blockExtender = blockExtender;
        size = blockExtender.filter.getSize();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(blacklist = new GuiButton(0, width / 2 - 40, height / 2 - 100, 80, 20, blockExtender.blacklist ? "Blacklist" : "Whitelist"));
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

        for (int i = 0; i < ITEMS_PER_SCREEN; i++)
        {
            int itemPlace = i + index;
            fontRenderer.drawString(blockExtender.filter.getName(itemPlace), width / 2 - 35, height / 2 - 66 + i * ITEM_SIZE, 0);
        }
    }

    /**
     * Handles mouse input.
     */
    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            if (i > 0)
            {
                i = 1;
            }

            if (i < 0)
            {
                i = -1;
            }
            setIndex(index - i);
        }
    }

    public void setIndex(int index)
    {
        int i = index;
        if (i < 0)
        {
            i = 0;
        }
        if (i > size - ITEMS_PER_SCREEN)
        {
            i = size - ITEMS_PER_SCREEN;
        }
        this.index = i;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        blacklist.displayString = blockExtender.blacklist ? "Blacklist" : "Whitelist";
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch (guibutton.id)
        {
            case 0:
                blockExtender.blacklist = !blockExtender.blacklist;
                break;
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int type)
    {
        super.mouseClicked(x, y, type);
        if (type == 0)
        {
            if (x >= width / 2 - 75 && x <= width / 2 + 75)
            {
                for (int i = 0; i < ITEMS_PER_SCREEN; i++)
                {
                    if (y >= height / 2 - 70 + i * ITEM_SIZE && y <= height / 2 - 70 + (1 + i) * ITEM_SIZE)
                    {
                        blockExtender.filter.setValue(index + i, !blockExtender.filter.getValue(index + i));
                    }
                }
            }
        }
    }

    private void drawContainerBackground()
    {
        int xSize = 176;
        int ySize = 153;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_FILTERED_BLOCK_EXTENDER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
        for (int i = 0; i < ITEMS_PER_SCREEN; i++)
        {
            this.drawTexturedModalRect(width / 2 - 75, height / 2 - 70 + i * ITEM_SIZE, 0, 154, 150, 14);
            int itemPlace = i + index;
            if (blockExtender.filter.getValue(itemPlace))
                this.drawTexturedModalRect(width / 2 - 75, height / 2 - 70 + i * ITEM_SIZE, 165, 154, 14, 14);
            else
                this.drawTexturedModalRect(width / 2 - 75, height / 2 - 70 + i * ITEM_SIZE, 151, 154, 14, 14);
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
