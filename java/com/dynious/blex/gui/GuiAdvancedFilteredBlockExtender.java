package com.dynious.blex.gui;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.TileAdvancedFilteredBlockExtender;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedFilteredBlockExtender extends GuiScreen
{
    private TileAdvancedFilteredBlockExtender blockExtender;
    private GuiButton spreadItems;

    public GuiAdvancedFilteredBlockExtender(TileAdvancedFilteredBlockExtender blockExtender)
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
        this.buttonList.add(spreadItems = new GuiButton(0, width/2 - 80, height/2 - 10, 80, 20, blockExtender.spreadItems? "Spread on": "Spread off"));
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

        for (int i = 0; i < blockExtender.getInsertDirection().length; i++)
        {
            ForgeDirection direction = ForgeDirection.getOrientation(blockExtender.getInsertDirection()[i]);
            if (i != blockExtender.getConnectedDirection().ordinal())
            {
                String letter = Character.toString(direction.toString().charAt(0));
                switch(i)
                {
                    //Bottom
                    case 0:
                        fontRenderer.drawString(letter, width / 2 + 28 + 34 + 4, height / 2 + 10 + 4, 0);
                        break;
                    //Top
                    case 1:
                        fontRenderer.drawString(letter, width / 2 + 28 + 17 + 4, height / 2 - 7 + 4, 0);
                        break;
                    //North
                    case 2:
                        fontRenderer.drawString(letter, width / 2 + 28 + 17 + 4, height / 2 - 24 + 4, 0);
                        break;
                    //South
                    case 3:
                        fontRenderer.drawString(letter, width / 2 + 28 + 17 + 4, height / 2 + 10 + 4, 0);
                        break;
                    //West
                    case 4:
                        fontRenderer.drawString(letter, width / 2 + 28 + 4, height / 2 - 7 + 4, 0);
                        break;
                    //East
                    case 5:
                        fontRenderer.drawString(letter, width / 2 + 28 + 34 + 4, height / 2 - 7 + 4, 0);
                        break;
                }
            }
        }
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

    @Override
    protected void mouseClicked(int x, int y, int type)
    {
        super.mouseClicked(x, y, type);
        if (type == 0)
        {
            //Bottom
            if (x >= width / 2 + 28 + 34 && x <= width / 2 + 28 + 34 + 14 && y >= height / 2 + 10 && y <= height / 2 + 10 + 14)
                blockExtender.setInsertDirection(0, blockExtender.getInsertDirection()[0]+ 1);
            //Top
            if (x >= width / 2 + 28 + 17 && x <= width / 2 + 28 + 17 + 14 && y >= height / 2 - 7 && y <= height / 2 - 7 + 14)
                blockExtender.setInsertDirection(1, blockExtender.getInsertDirection()[1]+ 1);
            //North
            if (x >= width / 2 + 28 + 17 && x <= width / 2 + 28 + 17 + 14 && y >= height / 2 - 24 && y <= height / 2 - 24 + 14)
                blockExtender.setInsertDirection(2, blockExtender.getInsertDirection()[2]+ 1);
            //South
            if (x >= width / 2 + 28 + 17 && x <= width / 2 + 28 + 17 + 14 && y >= height / 2 + 10 && y <= height / 2 + 10 + 14)
                blockExtender.setInsertDirection(3, blockExtender.getInsertDirection()[3]+ 1);
            //West
            if (x >= width / 2 + 28 && x <= width / 2 + 28 + 14 && y >= height / 2 - 7 && y <= height / 2 - 7 + 14)
                blockExtender.setInsertDirection(4, blockExtender.getInsertDirection()[4]+ 1);
            //East
            if (x >= width / 2 + 28 + 34 && x <= width / 2 + 28 + 34 + 14 && y >= height / 2 - 7 && y <= height / 2 - 7 + 14)
                blockExtender.setInsertDirection(5, blockExtender.getInsertDirection()[5]+ 1);
        }
    }

    private void drawContainerBackground()
    {
        int xSize = 256;
        int ySize = 153;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_ADVANCED_FILTERED_BLOCK_EXTENDER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
        for (int i = 0; i < ForgeDirection.values().length; i++)
        {
            boolean connection = false;
            if (ForgeDirection.getOrientation(i) == blockExtender.getConnectedDirection())
            {
                connection = true;
            }
            boolean hasTile = blockExtender.getTiles()[i] != null;
            switch(i)
            {
                //Bottom
                case 0:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28 + 34, height / 2 + 10, 207, 154, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28 + 34, height / 2 + 10, hasTile ? 179 : 193, 154, 14, 14);
                    break;
                //Top
                case 1:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 - 7, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 - 7, 176, hasTile ? 0 : 14, 14, 14);
                    break;
                //North
                case 2:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 - 24, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 - 24, 176, hasTile ? 0 : 14, 14, 14);
                    break;
                //South
                case 3:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 + 10, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 + 10, 176, hasTile ? 0 : 14, 14, 14);
                    break;
                //West
                case 4:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28, height / 2 - 7, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28, height / 2 - 7, 176, hasTile ? 0 : 14, 14, 14);
                    break;
                //East
                case 5:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28 + 34, height / 2 - 7, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28 + 34, height / 2 - 7, 176, hasTile ? 0 : 14, 14, 14);
                    break;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
