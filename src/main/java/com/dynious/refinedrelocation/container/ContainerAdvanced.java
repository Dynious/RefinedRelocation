package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.lib.GuiNetworkIds;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerAdvanced extends ContainerHierarchical implements IContainerAdvanced
{
    public static final int MESSAGE_SPREAD_ITEMS = 0;

    public IAdvancedTile tile;

    private boolean lastSpreadItems = false;
    private byte[] lastInsertDirection = {1, 1, 1, 1, 1, 1, 1};
    private byte lastMaxStackSize = 64;
    private boolean initialUpdate = true;

    public ContainerAdvanced(IAdvancedTile tile)
    {
        this.tile = tile;
    }

    public ContainerAdvanced(IAdvancedTile tile, ContainerHierarchical parentContainer)
    {
        super(parentContainer);
        this.tile = tile;
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

        for (int i = 0; i < tile.getInsertDirection().length; i++)
        {
            if (tile.getInsertDirection()[i] != lastInsertDirection[i] || initialUpdate)
            {
                for (Object crafter : crafters)
                {
                    ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), GuiNetworkIds.ADVANCED_BASE + i, tile.getInsertDirection()[i]);
                }
                lastInsertDirection[i] = tile.getInsertDirection()[i];
            }
        }

        int progressBarId = tile.getInsertDirection().length;

        if (tile.getMaxStackSize() != lastMaxStackSize || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), GuiNetworkIds.ADVANCED_BASE + progressBarId, tile.getMaxStackSize());
            }
            lastMaxStackSize = tile.getMaxStackSize();
        }

        progressBarId++;

        if (tile.getSpreadItems() != lastSpreadItems || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), GuiNetworkIds.ADVANCED_BASE + progressBarId, tile.getSpreadItems() ? 1 : 0);
            }
            lastSpreadItems = tile.getSpreadItems();
        }

        if (initialUpdate)
            initialUpdate = false;
    }

    @Override
    public void updateProgressBar(int id, int value)
    {
        if (id > GuiNetworkIds.ADVANCED_MAX || id < GuiNetworkIds.ADVANCED_BASE)
            return;

        id -= GuiNetworkIds.ADVANCED_BASE;
        if (id < tile.getInsertDirection().length)
        {
            setInsertDirection(id, value);
        }
        else
        {
            id -= tile.getInsertDirection().length;
            switch (id)
            {
                case 0:
                    setMaxStackSize((byte) value);
                    break;
                case 1:
                    setSpreadItems(value != 0);
                    break;
            }
        }
    }

    @Override
    public void setInsertDirection(int from, int value)
    {
        tile.setInsertDirection(from, value);
        lastInsertDirection[from] = (byte) value;
    }

    @Override
    public void setMaxStackSize(byte maxStackSize)
    {
        tile.setMaxStackSize(maxStackSize);
        lastMaxStackSize = maxStackSize;
    }

    @Override
    public void setSpreadItems(boolean spreadItems)
    {
        tile.setSpreadItems(spreadItems);
        lastSpreadItems = spreadItems;
    }

    @Override
    public void onMessage(int messageId, Object value, EntityPlayer player) {
        switch(messageId) {
            case MESSAGE_SPREAD_ITEMS: setSpreadItems((Boolean) value); break;
        }
    }
}
