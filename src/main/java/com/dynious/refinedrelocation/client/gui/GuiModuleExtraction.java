package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.GuiButtonTicksBetweenExtractions;
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

        new GuiButtonTicksBetweenExtractions(this, width/2 - 12, height/2 - 10, module);
    }
}
