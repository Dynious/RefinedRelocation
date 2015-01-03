package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneaky;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerModuleSneaky extends ContainerHierarchical
{
    public RelocatorModuleSneaky module;

    public ContainerModuleSneaky(RelocatorModuleSneaky module)
    {
        this.module = module;
        registerFieldSync("outputSide", module);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    public void setSide(int ticks)
    {
        module.setOutputSide(ticks);
    }

    @Override
    public void onMessage(int messageID, Object message)
    {
        switch (messageID)
        {
            case 0:
                setSide((Integer) message);
                break;
        }
    }
}
