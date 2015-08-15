package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonExtractionSide;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonModuleMaxStackSize;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonPulseExtractionToggle;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonTicksBetweenExtractions;
import com.dynious.refinedrelocation.container.ContainerModuleSneakyExtraction;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneakyExtraction;

public class GuiModuleSneakyExtraction extends GuiModular
{
    private RelocatorModuleSneakyExtraction module;

    public GuiModuleSneakyExtraction(RelocatorModuleSneakyExtraction module)
    {
        super(new ContainerModuleSneakyExtraction(module));
        this.module = module;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        new GuiButtonTicksBetweenExtractions(this, 0, 0, module);
        new GuiButtonExtractionSide(this, 0, 0, module);
        new GuiButtonPulseExtractionToggle(this, 0, 0, module);
        new GuiButtonModuleMaxStackSize(this, 0, 0, module);
    }
}
