package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

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
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            ((GuiHome)parent).onButtonClicked(index);
        }
    }
}
