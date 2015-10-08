package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonSide;
import com.dynious.refinedrelocation.container.ContainerModuleSneaky;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneaky;

public class GuiModuleSneaky extends GuiRefinedRelocationContainer {
    private RelocatorModuleSneaky module;

    public GuiModuleSneaky(RelocatorModuleSneaky module) {
        super(new ContainerModuleSneaky(module));
        this.module = module;

        xSize = 64;
        ySize = 48;
    }

    @Override
    public void initGui() {
        super.initGui();

        new GuiButtonSide(this, width / 2 - 17, height / 2 - 10, module);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        drawWindow(guiLeft, guiTop, xSize, ySize);
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }
}
