package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilter;
import com.dynious.refinedrelocation.api.tileentity.ISpecialSortingInventory;
import com.dynious.refinedrelocation.api.tileentity.grid.SpecialLocalizedStack;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.mods.BarrelFilter;
import mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel;
import mcp.mobius.betterbarrels.network.BarrelPacketHandler;
import mcp.mobius.betterbarrels.network.Message0x01ContentUpdate;
import net.minecraft.item.ItemStack;

public class TileSortingBarrel extends TileEntityBarrel implements ISpecialSortingInventory
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
    public boolean canUpdate()
    {
        return isFirstRun || super.canUpdate();
    }

    @Override
    public final boolean putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        super.setInventorySlotContents(slotIndex, itemStack);
        return true;
    }

    @Override
    public ItemStack putInInventory(ItemStack itemStack, boolean simulate)
    {
        if (getStorage().sameItem(itemStack.copy()))
        {
            int added = getStorage().isVoid() ? itemStack.stackSize : Math.min(getStorage().getMaxStoredCount() - getStorage().getAmount(), itemStack.stackSize);
            if (!simulate)
            {
                getStorage().setAmount(Math.min(getStorage().getAmount() + added, getStorage().getMaxStoredCount()));
                BarrelPacketHandler.INSTANCE.sendToDimension(new Message0x01ContentUpdate(this), getWorldObj().provider.dimensionId);
            }
            itemStack.stackSize -= added;
            if (itemStack.stackSize == 0)
                return null;
        }
        return itemStack;
    }

    @Override
    public SpecialLocalizedStack getLocalizedStackInSlot(int slot)
    {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null)
        {
            return new SpecialLocalizedStack(itemStack, this, slot, getStorage().getAmount());
        }
        return null;
    }

    @Override
    public void alterStackSize(int slot, int alteration)
    {
        getStorage().setAmount(getStorage().getAmount() + alteration);
    }

    @Override
    public ItemStack getStackInSlot(int islot)
    {
        /*ItemStack itemStack = super.getStackInSlot(islot);
        if (itemStack != null && getStorage() != null) {
            itemStack = itemStack.copy();
            if (getStorage().isCreative()) {
                itemStack.stackSize = itemStack.getMaxStackSize();
            } else {
                itemStack.stackSize = getStorage().getAmount();
            }
        }
        return itemStack;*/
        return super.getStackInSlot(islot);
    }

    @Override
    public void invalidate()
    {
        sortingInventoryHandler.onTileRemoved();
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        sortingInventoryHandler.onTileRemoved();
        super.onChunkUnload();
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        getHandler().onInventoryChange();
    }

    @Override
    public IFilter getFilter()
    {
        return filter;
    }

    @Override
    public Priority getPriority()
    {
        return Priority.NORMAL_HIGH;
    }

    @Override
    public void setPriority(Priority priority)
    {
        //NOOP
    }

    @Override
    public ISortingInventoryHandler getHandler()
    {
        return sortingInventoryHandler;
    }
}
