package com.dynious.blex.gui.container;

import com.dynious.blex.lib.GuiNetworkIds;
import com.dynious.blex.tileentity.IAdvancedFilteredTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerAdvancedFiltered extends ContainerHierarchical implements IContainerAdvancedFiltered
{

    public IAdvancedFilteredTile tile;

    // delegates
    private IContainerFiltered containerFiltered;
    private IContainerAdvanced containerAdvanced;

    private boolean lastRestrictExtraction = false;
    private boolean initialUpdate = true;

    public ContainerAdvancedFiltered(InventoryPlayer invPlayer, IAdvancedFilteredTile tile)
    {
        this.tile = tile;

        this.containerFiltered = new ContainerFiltered(invPlayer, tile, this);
        this.containerAdvanced = new ContainerAdvanced(invPlayer, tile, this);
    }

    public ContainerAdvancedFiltered(InventoryPlayer invPlayer, IAdvancedFilteredTile tile, ContainerHierarchical parentContainer)
    {
        super(parentContainer);

        this.tile = tile;

        this.containerFiltered = new ContainerFiltered(invPlayer, tile, this);
        this.containerAdvanced = new ContainerAdvanced(invPlayer, tile, this);
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
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), GuiNetworkIds.FILTERED_ADVANCED_BASE + 0, tile.getRestrictExtraction() ? 1 : 0);
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
    public void setUserFilter(String filter)
    {
        containerFiltered.setUserFilter(filter);
    }

    public void setBlackList(boolean value)
    {
        containerFiltered.setBlackList(value);
    }

    public void setFilterOption(int filterIndex, boolean value)
    {
        containerFiltered.setFilterOption(filterIndex, value);
    }

    public void toggleFilterOption(int filterIndex)
    {
        containerFiltered.toggleFilterOption(filterIndex);
    }

    public void setInsertDirection(int from, int value)
    {
        containerAdvanced.setInsertDirection(from, value);
    }

    public void setMaxStackSize(byte maxStackSize)
    {
        containerAdvanced.setMaxStackSize(maxStackSize);
    }

    public void setSpreadItems(boolean spreadItems)
    {
        containerAdvanced.setSpreadItems(spreadItems);
    }
    // end delegate methods

}
