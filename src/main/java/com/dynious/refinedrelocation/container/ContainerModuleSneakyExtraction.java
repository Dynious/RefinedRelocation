package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneakyExtraction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerModuleSneakyExtraction extends ContainerModuleExtraction
{
    private int extractionSide = 0;

    public ContainerModuleSneakyExtraction(RelocatorModuleSneakyExtraction module)
    {
        super(module);
    }

    @Override
    public void detectAndSendChanges()
    {
        if (((RelocatorModuleSneakyExtraction) module).getExtractionSide() != extractionSide || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), 3, ((RelocatorModuleSneakyExtraction) module).getExtractionSide());
            }
            extractionSide = ((RelocatorModuleSneakyExtraction) module).getExtractionSide();
        }
        super.detectAndSendChanges();
    }

    @Override
    public void updateProgressBar(int id, int value)
    {
        super.updateProgressBar(id, value);

        switch (id)
        {
            case 3:
                setExtractionSide(value);
                break;
        }
    }

    public void setExtractionSide(int side)
    {
        ((RelocatorModuleSneakyExtraction) module).setExtractionSide(side);
    }

    @Override
    public void onMessageInteger(int messageId, int value, EntityPlayer player)
    {
        super.onMessageInteger(messageId, value, player);
        if (messageId == 3)
        {
            setExtractionSide(value);
        }
    }

}
