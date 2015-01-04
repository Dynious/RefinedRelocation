package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.filter.IFilterModule;
import com.dynious.refinedrelocation.api.tileentity.INewFilterTile;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.grid.FilterModuleRegistry;
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
        registerFieldSync("blacklists", tile);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (tile instanceof ISortingInventory)
        {
            ISortingInventory inv = (ISortingInventory) tile;
            if (inv.getPriority().ordinal() != lastPriority || initialUpdate)
            {
                for (Object crafter : crafters)
                {
                    ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), 0, inv.getPriority().ordinal());
                }
                lastPriority = inv.getPriority().ordinal();
            }
        }

        if (initialUpdate)
            initialUpdate = false;
    }

    @Override
    public void onMessage(int messageID, Object message)
    {
        if (messageID == 0)
        {
            int i = (Integer) message;
            int index = i >> 8;
            int id = i & 0xFF;
            IFilterModule module = FilterModuleRegistry.getNew(index);
            module.init(tile);
            tile.getFilter().filters[id] = module;
            tile.onFilterChanged();
        }
    }

    @Override
    public void updateProgressBar(int id, int value)
    {
        switch (id)
        {
            case 0:
                setPriority(value);
                break;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    public void setPriority(int priority)
    {
        lastPriority = priority;
        if (tile instanceof ISortingInventory)
            ((ISortingInventory) tile).setPriority(ISortingInventory.Priority.values()[priority]);
    }
}
