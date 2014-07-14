package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.GuiHome;
import net.minecraft.client.Minecraft;

public class GuiButtonOpenModuleGUI extends GuiScalableButton
{
    private int index;

    public GuiButtonOpenModuleGUI(GuiHome parent, int index, String buttonText)
    {
        super(parent, 0, 0, Minecraft.getMinecraft().fontRenderer.getStringWidth(buttonText) + 3*2, 20, 0, 0, buttonText);
        this.index = index;
        update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);

        if (isMouseInsideBounds(mouseX, mouseY))
        {
            ((GuiHome)parent).onButtonClicked(index);
        }
    }
}
