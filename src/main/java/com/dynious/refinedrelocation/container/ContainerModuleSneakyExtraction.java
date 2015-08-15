package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneakyExtraction;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIInteger;
import net.minecraft.entity.player.EntityPlayer;

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
            sendSyncMessage(new MessageGUIInteger(3, ((RelocatorModuleSneakyExtraction) module).getExtractionSide()));
            extractionSide = ((RelocatorModuleSneakyExtraction) module).getExtractionSide();
        }
        super.detectAndSendChanges();
    }

    public void setExtractionSide(int side)
    {
        ((RelocatorModuleSneakyExtraction) module).setExtractionSide(side);
    }

    @Override
    public void onMessageInteger(int messageId, int value, EntityPlayer player)
    {
        super.onMessageInteger(messageId, value, player);
        if(isRestrictedAccessWithError(player)) {
            return;
        }
        switch(messageId) {
            case 3: setExtractionSide(value); break;
        }
    }

}
