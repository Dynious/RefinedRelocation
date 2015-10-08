package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonExtractionSide;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonModuleMaxStackSize;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonPulseExtractionToggle;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonTicksBetweenExtractions;
import com.dynious.refinedrelocation.container.ContainerModuleSneakyExtraction;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneakyExtraction;

public class GuiModuleSneakyExtraction extends GuiRefinedRelocationContainer {
    private RelocatorModuleSneakyExtraction module;

    public GuiModuleSneakyExtraction(RelocatorModuleSneakyExtraction module) {
        super(new ContainerModuleSneakyExtraction(module));
        this.module = module;
        xSize = 142;
        ySize = 48;
    }

    @Override
    public void initGui() {
        super.initGui();

        final int i = 14;
        new GuiButtonTicksBetweenExtractions(this, width / 2 - 46 - i, height / 2 - 10, module);
        new GuiButtonExtractionSide(this, width / 2 - 17 - i, height / 2 - 10, module);
        new GuiButtonPulseExtractionToggle(this, width / 2 + 22 - i, height / 2 - 10, module);
        new GuiButtonModuleMaxStackSize(this, width / 2 + 51 - i, height / 2 - 10, module);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        drawWindow(guiLeft, guiTop, xSize, ySize);
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }
}
