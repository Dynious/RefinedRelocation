package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.tileentity.INewFilterTile;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerModularFiltered extends ContainerHierarchical
{
    public INewFilterTile tile;

    private boolean lastBlacklist = true;
    private boolean initialUpdate = true;
    private int lastPriority;

    public ContainerModularFiltered(INewFilterTile tile)
    {
        this.tile = tile;
    }

    public ContainerModularFiltered(INewFilterTile tile, ContainerHierarchical parentContainer)
    {
        super(parentContainer);
        this.tile = tile;
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (tile.getFilter().isBlacklisting() != lastBlacklist || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), 0, tile.getFilter().isBlacklisting() ? 1 : 0);
            }
            lastBlacklist = tile.getFilter().isBlacklisting();
        }

        if (tile instanceof ISortingInventory)
        {
            ISortingInventory inv = (ISortingInventory) tile;
            if (inv.getPriority().ordinal() != lastPriority || initialUpdate)
            {
                for (Object crafter : crafters)
                {
                    ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), 1, inv.getPriority().ordinal());
                }
                lastPriority = inv.getPriority().ordinal();
            }
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
                setBlackList(value != 0);
                break;
            case 1:
                setPriority(value);
                break;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    public void setBlackList(boolean value)
    {
        lastBlacklist = value;
        tile.getFilter().setBlacklists(value);
    }

    public void setPriority(int priority)
    {
        lastPriority = priority;
        if (tile instanceof ISortingInventory)
            ((ISortingInventory) tile).setPriority(ISortingInventory.Priority.values()[priority]);
    }
}
