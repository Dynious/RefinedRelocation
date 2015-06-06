package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiEditFilterButton extends GuiButton
{
    public static final int WIDTH = 31;
    public static final int HEIGHT = 26;

    public GuiEditFilterButton(int x, int y)
    {
        super(-1, x, y, WIDTH, HEIGHT, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        mc.getTextureManager().bindTexture(Resources.GUI_SHARED);
        boolean isInside = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
        drawTexturedModalRect(xPosition, yPosition, isInside ? 0 : 31, 204, width, height);
        drawTexturedModalRect(xPosition + width / 2 - 7, yPosition + height / 2 - 9, 152, 238, 18, 18);
    }
}
