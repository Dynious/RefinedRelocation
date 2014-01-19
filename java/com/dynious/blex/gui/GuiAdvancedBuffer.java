package com.dynious.blex.gui;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.TileAdvancedBuffer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedBuffer extends GuiScreen
{
    TileAdvancedBuffer buffer;
    GuiButton[] buttons = new GuiButton[6];

    public GuiAdvancedBuffer(TileAdvancedBuffer buffer)
    {
        this.buffer = buffer;
    }
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        for (int i = 0; i < 3; i++)
        {
            this.buttonList.add(buttons[i] = new GuiButton(i, width/2 - 70 + (i*50), height/2 - 25, 40, 20, ForgeDirection.getOrientation(buffer.getInsertDirection()[i]).toString()));
        }
        for (int i = 0; i < 3; i++)
        {
            this.buttonList.add(buttons[i + 3] = new GuiButton(i + 3, width/2 - 70 + (i*50), height/2 + 15, 40, 20, ForgeDirection.getOrientation(buffer.getInsertDirection()[i + 3]).toString()));
        }
    }

    @Override
    public void drawScreen(int h, int j, float f)
    {
        drawDefaultBackground();
        drawContainerBackground();
        super.drawScreen(h, j, f);
        for (int i = 0; i < 3; i++)
        {
            this.fontRenderer.drawString(Integer.toString(i), width/2 - 52 + (i*50), height/2 - 35, 4210752);
        }
        for (int i = 0; i < 3; i++)
        {
            this.fontRenderer.drawString(Integer.toString(i + 3), width/2 - 52 + (i*50), height/2 + 5, 4210752);
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i].displayString = ForgeDirection.getOrientation(buffer.getInsertDirection()[i]).toString();
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        buffer.setInsertDirection(guibutton.id, buffer.getInsertDirection()[guibutton.id] + 1);
    }

    private void drawContainerBackground()
    {
        int xSize = 204;
        int ySize = 88;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_ADVANCED_BUFFER);
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
