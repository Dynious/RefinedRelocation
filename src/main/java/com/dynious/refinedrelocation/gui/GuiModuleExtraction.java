package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.gui.container.ContainerModuleExtraction;
import com.dynious.refinedrelocation.gui.widget.GuiButtonTicksBetweenExtractions;
import com.dynious.refinedrelocation.lib.Resources;
import org.lwjgl.opengl.GL11;

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
