package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneaky;
import com.dynious.refinedrelocation.gui.container.ContainerModuleSneaky;
import com.dynious.refinedrelocation.gui.widget.GuiButtonSide;
import com.dynious.refinedrelocation.lib.Resources;
import org.lwjgl.opengl.GL11;

public class GuiModuleSneaky extends GuiRefinedRelocationContainer
{
    private RelocatorModuleSneaky module;

    public GuiModuleSneaky(RelocatorModuleSneaky module)
    {
        super(new ContainerModuleSneaky(module));
        this.module = module;
        this.xSize = 123;
        this.ySize = 45;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        new GuiButtonSide(this, width/2 - 17, height/2 - 10, module);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI_POWER_LIMITER);
        int xPos = (width - xSize) / 2;
        int yPos = (height - ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(f, i, j);
    }
}
