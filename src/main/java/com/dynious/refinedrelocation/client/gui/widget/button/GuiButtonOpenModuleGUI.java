package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.GuiModuleMultiModule;
import net.minecraft.client.Minecraft;

public class GuiButtonOpenModuleGUI extends GuiScalableButton
{
    private int index;

    public GuiButtonOpenModuleGUI(GuiModuleMultiModule parent, int index, String buttonText)
    {
        super(parent, 0, 0, Minecraft.getMinecraft().fontRenderer.getStringWidth(buttonText) + 3 * 2, 20, null, buttonText);
        this.index = index;
        update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);

        if (isInsideBounds(mouseX, mouseY))
        {
            ((GuiModuleMultiModule) parent).onButtonClicked(index);
        }
    }
}
