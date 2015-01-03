package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerModuleExtraction extends ContainerHierarchical
{
    public RelocatorModuleExtraction module;

    public ContainerModuleExtraction(RelocatorModuleExtraction module)
    {
        this.module = module;
        registerFieldSync("ticksBetweenExtraction", module);
        registerFieldSync("redstoneControlState", module);
        registerFieldSync("maxExtractionStackSize", module);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    public void setTicksBetweenExtraction(int ticks)
    {
        module.setTicksBetweenExtraction(ticks);
    }

    public void setRedstoneControlState(int state)
    {
        module.redstoneControlState = state;
    }

    public void setMaxStackSize(int maxStackSize)
    {
        module.maxExtractionStackSize = maxStackSize;
    }

    @Override
    public void onMessage(int messageID, Object message)
    {
        switch (messageID)
        {
            case 0:
                setTicksBetweenExtraction((Integer) message);
                break;
            case 1:
                setRedstoneControlState((Integer) message);
                break;
            case 2:
                setMaxStackSize((Integer) message);
                break;
        }
    }
}
