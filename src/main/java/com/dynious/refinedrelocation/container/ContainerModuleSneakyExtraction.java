package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneakyExtraction;

public class ContainerModuleSneakyExtraction extends ContainerModuleExtraction
{
    public ContainerModuleSneakyExtraction(RelocatorModuleSneakyExtraction module)
    {
        super(module);
        registerFieldSync("extractionSide", module);
    }

    public void setExtractionSide(int side)
    {
        ((RelocatorModuleSneakyExtraction) module).setExtractionSide(side);
    }

    @Override
    public void onMessage(int messageID, Object message)
    {
        super.onMessage(messageID, message);
        if (messageID == 3)
            setExtractionSide((Integer) message);
    }
}
