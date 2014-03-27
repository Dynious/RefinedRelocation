package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilter;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.mods.BarrelFilter;
import mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel;
import mcp.mobius.betterbarrels.network.BarrelPacketHandler;
import mcp.mobius.betterbarrels.network.Message0x01ContentUpdate;
import net.minecraft.item.ItemStack;

public class TileSortingBarrel extends TileEntityBarrel implements ISortingInventory
{
    public boolean isFirstRun = true;

    private BarrelFilter filter = new BarrelFilter(this);

    private ISortingInventoryHandler sortingInventoryHandler = APIUtils.createSortingInventoryHandler(this);

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
    public ItemStack putInInventory(ItemStack itemStack)
    {
        int added = getStorage().addStack(itemStack.copy());
        if (added != 0)
        {
            itemStack.stackSize -= added;
            BarrelPacketHandler.INSTANCE.sendToDimension(new Message0x01ContentUpdate(this), getWorldObj().provider.dimensionId);
            if (itemStack.stackSize == 0)
                return null;
        }
        return itemStack;
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
    public ISortingInventoryHandler getSortingHandler()
    {
        return sortingInventoryHandler;
    }
}
