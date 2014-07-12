package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneaky;
import com.dynious.refinedrelocation.gui.container.ContainerModuleSneaky;
import com.dynious.refinedrelocation.gui.widget.GuiButtonSide;
import com.dynious.refinedrelocation.lib.Resources;
import org.lwjgl.opengl.GL11;

public class GuiModuleSneaky extends GuiModular
{
    private RelocatorModuleSneaky module;

    public GuiModuleSneaky(RelocatorModuleSneaky module)
    {
        super(new ContainerModuleSneaky(module));
        this.module = module;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        new GuiButtonSide(this, width/2 - 17, height/2 - 10, module);
    }
}
