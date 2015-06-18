package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonModuleMaxStackSize;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonPulseExtractionToggle;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonTicksBetweenExtractions;
import com.dynious.refinedrelocation.container.ContainerModuleExtraction;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;

public class GuiModuleExtraction extends GuiModular
{
    private RelocatorModuleExtraction module;

    public GuiModuleExtraction(RelocatorModuleExtraction module)
    {
        super(new ContainerModuleExtraction(module));
        this.module = module;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        new GuiButtonTicksBetweenExtractions(this, width / 2 - 12, height / 2 - 10, module);
        new GuiButtonPulseExtractionToggle(this, width / 2 - 30, height / 2 - 20, module);
        new GuiButtonModuleMaxStackSize(this, 0, 0, module);
    }
}
