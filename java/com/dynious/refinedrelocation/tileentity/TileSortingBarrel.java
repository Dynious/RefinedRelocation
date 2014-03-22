package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.filter.IFilter;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.SortingInventoryHandler;
import com.dynious.refinedrelocation.mods.BarrelFilter;
import com.dynious.refinedrelocation.mods.BarrelSortingInventoryHandler;
import mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel;
import net.minecraft.item.ItemStack;

public class TileSortingBarrel extends TileEntityBarrel implements ISortingInventory
{
    public boolean isFirstRun = true;

    private BarrelFilter filter = new BarrelFilter(this);

    private BarrelSortingInventoryHandler sortingInventoryHandler = new BarrelSortingInventoryHandler(this);

    @Override
    public void updateEntity()
    {
        if (isFirstRun)
        {
            sortingInventoryHandler.onTileAdded();
            isFirstRun = false;
        }
        super.updateEntity();
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (!getStorage().hasItem())
        {
            super.setInventorySlotContents(i, itemstack);
        }
        else
        {
            sortingInventoryHandler.setInventorySlotContents(i, itemstack);
        }
    }

    @Override
    public ItemStack[] getInventory()
    {
        return null;
    }

    @Override
    public IFilter getFilter()
    {
        return filter;
    }

    @Override
    public Priority getPriority()
    {
        return Priority.HIGH;
    }

    @Override
    public SortingInventoryHandler getSortingHandler()
    {
        return sortingInventoryHandler;
    }
}
