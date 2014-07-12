package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneakyExtraction;
import com.dynious.refinedrelocation.gui.container.ContainerModuleSneakyExtraction;
import com.dynious.refinedrelocation.gui.widget.GuiButtonExtractionSide;
import com.dynious.refinedrelocation.gui.widget.GuiButtonTicksBetweenExtractions;

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
