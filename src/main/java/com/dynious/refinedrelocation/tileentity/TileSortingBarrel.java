package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilter;
import com.dynious.refinedrelocation.api.tileentity.ISpecialSortingInventory;
import com.dynious.refinedrelocation.api.tileentity.grid.SpecialLocalizedStack;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.mods.BarrelFilter;
import com.dynious.refinedrelocation.mods.JabbaHelper;
import mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel;
import mcp.mobius.betterbarrels.common.items.ItemBarrelHammer;
import mcp.mobius.betterbarrels.network.BarrelPacketHandler;
import mcp.mobius.betterbarrels.network.Message0x01ContentUpdate;
import net.minecraft.entity.player.EntityPlayer;
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
                getStorage().addStack(itemStack);
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
        if (getStorage().hasItem())
        {
            if (alteration > 0)
            {
                ItemStack stack = getStorage().getItem().copy();
                stack.stackSize = alteration;
                getStorage().addStack(stack);
            }
            else
            {
                while (alteration > 0)
                {
                    int s = Math.min(-alteration, getStorage().getItem().getMaxStackSize());
                    alteration += s;
                    getStorage().getStack(s);
                }
            }
        }
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

    @Override
    public void rightClick(EntityPlayer player, int side)
    {
        ItemStack heldItem = player.getHeldItem();
        if (!player.isSneaking())
        {
            if (heldItem != null && (heldItem.getItem() instanceof ItemBarrelHammer))
            {
                if (JabbaHelper.removeSortingUpgradeFromBarrel(this))
                {
                    player.dropPlayerItemWithRandomChoice(new ItemStack(ModItems.sortingUpgrade, 1, 1), false);
                }
                return;
            }
        }
        super.rightClick(player, side);


    }
}
