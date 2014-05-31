package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.gui.container.ContainerModuleExtraction;
import com.dynious.refinedrelocation.gui.widget.GuiButtonTicksBetweenExtractions;
import com.dynious.refinedrelocation.lib.Resources;
import org.lwjgl.opengl.GL11;

public class GuiModuleExtraction extends GuiRefinedRelocationContainer
{
    private RelocatorModuleExtraction module;

    public GuiModuleExtraction(RelocatorModuleExtraction module)
    {
        super(new ContainerModuleExtraction(module));
        this.xSize = 0;
        this.ySize = 0;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        new GuiButtonTicksBetweenExtractions(this, 0, 0, module);
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
