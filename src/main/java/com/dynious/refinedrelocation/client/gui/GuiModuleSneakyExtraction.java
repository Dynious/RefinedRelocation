package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.GuiButtonExtractionSide;
import com.dynious.refinedrelocation.client.gui.widget.GuiButtonTicksBetweenExtractions;
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
    }
}
