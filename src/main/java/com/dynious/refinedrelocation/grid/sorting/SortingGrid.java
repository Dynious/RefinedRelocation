package com.dynious.refinedrelocation.grid.sorting;

import com.dynious.refinedrelocation.api.filter.FilterResult;
import com.dynious.refinedrelocation.api.tileentity.IInventoryChangeListener;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.ISpecialSortingInventory;
import com.dynious.refinedrelocation.api.tileentity.grid.ISortingGrid;
import com.dynious.refinedrelocation.api.tileentity.grid.LocalizedStack;
import com.dynious.refinedrelocation.api.tileentity.handlers.IGridMemberHandler;
import com.dynious.refinedrelocation.grid.Grid;
import com.dynious.refinedrelocation.grid.MultiFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SortingGrid extends Grid implements ISortingGrid
{
    /**
     * Filters an ItemStack to all members of the SortingMember group
     *
     * @param itemStack The ItemStack to be filtered to all childs and this SortingMember
     * @return The ItemStack that was not able to fit in any ISortingInventory
     */
    public ItemStack filterStackToGroup(ItemStack itemStack, TileEntity requester, int slot, boolean simulate)
    {
        if (members != null && !members.isEmpty())
        {
            int bestPriority = Integer.MIN_VALUE;
            ISortingInventory bestInventory = null;
            List<ISortingInventory> sortingList = createSortingList(requester);
            FilterResult outResult = new FilterResult();
            for (ISortingInventory inventory : sortingList)
            {
                if (inventory.getFilter() instanceof MultiFilter)
                {
                    MultiFilter multiFilter = (MultiFilter) inventory.getFilter();
                    outResult.passes = false;
                    outResult.priorityBoost = 0;
                    if(multiFilter.passesFilter(itemStack, outResult)) {
                        if(inventory.getPriority().ordinal() + outResult.priorityBoost > bestPriority) {
                            bestInventory = inventory;
                            bestPriority = inventory.getPriority().ordinal() + outResult.priorityBoost;
                        }
                    }
                } else
                {
                    if (inventory.getFilter().passesFilter(itemStack) && inventory.getPriority().ordinal() > bestPriority)
                    {
                        bestInventory = inventory;
                        bestPriority = inventory.getPriority().ordinal();
                    }
                }

            }
            if (bestInventory == requester)
            {
                return itemStack;
            } else if (bestInventory != null)
            {
                itemStack = bestInventory.putInInventory(itemStack, simulate);
                if (itemStack == null || itemStack.stackSize == 0)
                {
                    return null;
                }
            }

        }
        return itemStack;
    }

    private List<ISortingInventory> createSortingList(TileEntity requester)
    {
        List<ISortingInventory> list = new ArrayList<ISortingInventory>();

        for (Iterator<IGridMemberHandler> iterator = members.iterator(); iterator.hasNext(); )
        {
            IGridMemberHandler filteringMember = iterator.next();
            if (filteringMember.getOwner().isInvalid())
            {
                iterator.remove();
                continue;
            }
            if (filteringMember.getOwner() instanceof ISortingInventory)
            {
                list.add((ISortingInventory) filteringMember.getOwner());
            }
        }
        return list;
    }

    @Override
    public List<LocalizedStack> getItemsInGrid()
    {
        List<LocalizedStack> list = new ArrayList<LocalizedStack>();
        if (members != null)
        {
            for (IGridMemberHandler member : members)
            {
                if (member.getOwner() instanceof ISortingInventory)
                {
                    ISortingInventory inventory = (ISortingInventory) member.getOwner();
                    for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
                    {
                        ItemStack stack = inventory.getStackInSlot(slot);
                        if (stack != null)
                        {
                            if (inventory instanceof ISpecialSortingInventory)
                            {
                                list.add(((ISpecialSortingInventory) inventory).getLocalizedStackInSlot(slot));
                            } else
                            {
                                list.add(new LocalizedStack(stack, inventory, slot));
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public void onInventoryChange()
    {
        if (members != null)
        {
            for (IGridMemberHandler member : members)
            {
                if (member.getOwner() instanceof IInventoryChangeListener)
                {
                    ((IInventoryChangeListener) member.getOwner()).onInventoryChanged();
                }
            }
        }
    }
}
