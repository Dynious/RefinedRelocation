package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIInteger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
            sendSyncMessage(new MessageGUIInteger(0, module.getTicksBetweenExtraction()));
            lastTicks = module.getTicksBetweenExtraction();
        }

        if (module.redstoneControlState != lastRedstoneControlState || initialUpdate)
        {
            sendSyncMessage(new MessageGUIInteger(1, module.redstoneControlState));
            lastRedstoneControlState = module.redstoneControlState;
        }

        if (module.maxExtractionStackSize != lastMaxExtractionStackSize || initialUpdate)
        {
            sendSyncMessage(new MessageGUIInteger(2, module.maxExtractionStackSize));
            lastMaxExtractionStackSize = module.maxExtractionStackSize;
        }

        if (initialUpdate)
            initialUpdate = false;
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
    public void onMessageInteger(int messageId, int value, EntityPlayer player)
    {
        if(!checkPermission(player)) {
            return;
        }
        switch (messageId)
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

}
