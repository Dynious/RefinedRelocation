package com.dynious.refinedrelocation.gui.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerModuleExtraction extends ContainerHierarchical
{
    public RelocatorModuleExtraction module;
    private boolean initialUpdate = true;

    private int lastTicks = 0;

    public ContainerModuleExtraction(RelocatorModuleExtraction module)
    {
        this.module = module;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (module.getTicksBetweenExtraction() != lastTicks || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), 0, module.getTicksBetweenExtraction());
            }
            lastTicks = module.getTicksBetweenExtraction();
        }

        if (initialUpdate)
            initialUpdate = false;
    }

    @Override
    public void updateProgressBar(int id, int value)
    {
        switch (id)
        {
            case 0:
                setTicksBetweenExtraction(value);
                break;
        }
    }

    public void setTicksBetweenExtraction(int ticks)
    {
        module.setTicksBetweenExtraction(ticks);
    }
}
