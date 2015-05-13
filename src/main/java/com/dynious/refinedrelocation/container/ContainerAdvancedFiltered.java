package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.lib.GuiNetworkIds;
import com.dynious.refinedrelocation.tileentity.IAdvancedFilteredTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerAdvancedFiltered extends ContainerHierarchical implements IContainerAdvancedFiltered
{
    public static final int MESSAGE_BLACKLIST = 0;
    public static final int MESSAGE_SPREAD_ITEMS = 1;
    public static final int MESSAGE_MAX_STACK_SIZE = 2;
    public static final int MESSAGE_RESTRICT_EXTRACTION = 3;

    public IAdvancedFilteredTile tile;

    // delegates
    private IContainerFiltered containerFiltered;
    private IContainerAdvanced containerAdvanced;

    private boolean lastRestrictExtraction = false;
    private boolean initialUpdate = true;

    public ContainerAdvancedFiltered(IAdvancedFilteredTile tile)
    {
        this.tile = tile;

        this.containerFiltered = new ContainerFiltered(tile, this);
        this.containerAdvanced = new ContainerAdvanced(tile, this);
    }

    public ContainerAdvancedFiltered(IAdvancedFilteredTile tile, ContainerHierarchical parentContainer)
    {
        super(parentContainer);

        this.tile = tile;

        this.containerFiltered = new ContainerFiltered(tile, this);
        this.containerAdvanced = new ContainerAdvanced(tile, this);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void detectAndSendChanges()
    {
        ((ContainerAdvanced) containerAdvanced).detectAndSendChanges();
        ((ContainerFiltered) containerFiltered).detectAndSendChanges();

        if (tile.getRestrictExtraction() != lastRestrictExtraction || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), GuiNetworkIds.FILTERED_ADVANCED_BASE, tile.getRestrictExtraction() ? 1 : 0);
            }
            lastRestrictExtraction = tile.getRestrictExtraction();
        }

        if (initialUpdate)
            initialUpdate = false;
    }

    @Override
    public void updateProgressBar(int id, int value)
    {
        ((ContainerAdvanced) containerAdvanced).updateProgressBar(id, value);
        ((ContainerFiltered) containerFiltered).updateProgressBar(id, value);

        if (id > GuiNetworkIds.FILTERED_ADVANCED_MAX || id < GuiNetworkIds.FILTERED_ADVANCED_BASE)
            return;

        id -= GuiNetworkIds.FILTERED_ADVANCED_BASE;

        switch (id)
        {
            case 0:
                tile.setRestrictionExtraction(value != 0);
                break;
        }
    }

    @Override
    public void setRestrictExtraction(boolean restrictExtraction)
    {
        tile.setRestrictionExtraction(restrictExtraction);
        lastRestrictExtraction = restrictExtraction;
    }

    // delegate methods
    @Override
    public void setUserFilter(String filter)
    {
        containerFiltered.setUserFilter(filter);
    }

    @Override
    public void setBlackList(boolean value)
    {
        containerFiltered.setBlackList(value);
    }

    @Override
    public void setFilterOption(int filterIndex, boolean value)
    {
        containerFiltered.setFilterOption(filterIndex, value);
    }

    @Override
    public void toggleFilterOption(int filterIndex)
    {
        containerFiltered.toggleFilterOption(filterIndex);
    }

    @Override
    public void setPriority(int priority)
    {
        //NOOP
    }

    @Override
    public void setInsertDirection(int from, int value)
    {
        containerAdvanced.setInsertDirection(from, value);
    }

    @Override
    public void setMaxStackSize(byte maxStackSize)
    {
        containerAdvanced.setMaxStackSize(maxStackSize);
    }

    @Override
    public void setSpreadItems(boolean spreadItems)
    {
        containerAdvanced.setSpreadItems(spreadItems);
    }
    // end delegate methods


    @Override
    public void onMessage(int messageId, Object value, EntityPlayer player) {
        switch(messageId) {
            case MESSAGE_BLACKLIST: setBlackList((Boolean) value); break;
            case MESSAGE_SPREAD_ITEMS: setSpreadItems((Boolean) value); break;
            case MESSAGE_MAX_STACK_SIZE: setMaxStackSize((Byte) value); break;
            case MESSAGE_RESTRICT_EXTRACTION: setRestrictExtraction((Boolean) value); break;
        }
    }
}
