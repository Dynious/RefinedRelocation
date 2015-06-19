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
            for (Object crafter : crafters)
            {
                NetworkHandler.INSTANCE.sendTo(new MessageGUIInteger(0, module.getTicksBetweenExtraction()), (EntityPlayerMP) crafter);
            }
            lastTicks = module.getTicksBetweenExtraction();
        }

        if (module.redstoneControlState != lastRedstoneControlState || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                NetworkHandler.INSTANCE.sendTo(new MessageGUIInteger(1, module.redstoneControlState), (EntityPlayerMP) crafter);
            }
            lastRedstoneControlState = module.redstoneControlState;
        }

        if (module.maxExtractionStackSize != lastMaxExtractionStackSize || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                NetworkHandler.INSTANCE.sendTo(new MessageGUIInteger(2, module.maxExtractionStackSize), (EntityPlayerMP) crafter);
            }
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
