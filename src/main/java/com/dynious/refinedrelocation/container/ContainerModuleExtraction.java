package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerModuleExtraction extends ContainerHierarchical
{
    public RelocatorModuleExtraction module;
    protected boolean initialUpdate = true;

    private int lastTicks = 0;
    private int lastRedstoneControlState = 0;
    private int lastMaxExtractionStackSize = 0;

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

        if (module.redstoneControlState != lastRedstoneControlState || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), 1, module.redstoneControlState);
            }
            lastRedstoneControlState = module.redstoneControlState;
        }

        if (module.maxExtractionStackSize != lastMaxExtractionStackSize || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), 2, module.maxExtractionStackSize);
            }
            lastMaxExtractionStackSize = module.maxExtractionStackSize;
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
            case 1:
                setRedstoneControlState(value);
                break;
            case 2:
                setMaxStackSize(value);
                break;

        }
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
    public void onMessage(int messageID, Object message, EntityPlayer entityPlayer)
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
