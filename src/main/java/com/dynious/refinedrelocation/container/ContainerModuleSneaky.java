package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneaky;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIInteger;
import net.minecraft.entity.player.EntityPlayer;

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
            sendSyncMessage(new MessageGUIInteger(0, module.getOutputSide()));
            lastSide = module.getOutputSide();
        }

        if (initialUpdate)
            initialUpdate = false;
    }

    public void setSide(int ticks)
    {
        module.setOutputSide(ticks);
    }

    @Override
    public void onMessageInteger(int messageId, int value, EntityPlayer player)
    {
        if(isRestrictedAccessWithError(player)) {
            return;
        }
        switch(messageId) {
            case 0: setSide(value); break;
        }
    }

}
