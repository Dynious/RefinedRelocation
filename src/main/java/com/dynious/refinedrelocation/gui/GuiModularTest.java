package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.gui.container.ContainerModuleExtraction;
import com.dynious.refinedrelocation.gui.widget.GuiButtonTicksBetweenExtractions;

public class GuiModularTest extends GuiModular
{
    private RelocatorModuleExtraction moduleExtraction;

    public GuiModularTest(RelocatorModuleExtraction moduleExtraction)
    {
        super(new ContainerModuleExtraction(moduleExtraction));
        this.moduleExtraction = moduleExtraction;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        new GuiButtonTicksBetweenExtractions(this, 100, 100, moduleExtraction);

        new GuiButtonTicksBetweenExtractions(this, 100, 100, moduleExtraction);
    }
}
