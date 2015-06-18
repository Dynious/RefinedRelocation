package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonSide;
import com.dynious.refinedrelocation.container.ContainerModuleSneaky;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneaky;

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

        new GuiButtonSide(this, width / 2 - 17, height / 2 - 10, module);
    }
}
