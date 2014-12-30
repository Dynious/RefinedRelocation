package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneaky;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerModuleSneaky extends ContainerHierarchical
{
    public RelocatorModuleSneaky module;
    private boolean initialUpdate = true;

    private int lastSide = 0;

    public ContainerModuleSneaky(RelocatorModuleSneaky module)
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

        if (module.getOutputSide() != lastSide || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), 0, module.getOutputSide());
            }
            lastSide = module.getOutputSide();
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
                setSide(value);
                break;
        }
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
