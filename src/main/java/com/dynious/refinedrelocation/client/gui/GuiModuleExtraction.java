package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonModuleMaxStackSize;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonPulseExtractionToggle;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonTicksBetweenExtractions;
import com.dynious.refinedrelocation.container.ContainerModuleExtraction;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;

public class GuiModuleExtraction extends GuiRefinedRelocationContainer {
    private RelocatorModuleExtraction module;

    public GuiModuleExtraction(RelocatorModuleExtraction module) {
        super(new ContainerModuleExtraction(module));
        this.module = module;

        xSize = 110;
        ySize = 48;
    }

    @Override
    public void initGui() {
        super.initGui();

        new GuiButtonTicksBetweenExtractions(this, width / 2 - 42, height / 2 - 10, module);
        new GuiButtonPulseExtractionToggle(this, width / 2 - 12, height / 2 - 10, module);
        new GuiButtonModuleMaxStackSize(this, width / 2 + 18, height / 2 - 10, module);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        drawWindow(guiLeft, guiTop, xSize, ySize);
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }
}
