package com.dynious.refinedrelocation.grid.sorting;

import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.grid.ISortingGrid;
import com.dynious.refinedrelocation.api.tileentity.handlers.IGridMemberHandler;
import com.dynious.refinedrelocation.grid.Grid;
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
    public ItemStack filterStackToGroup(ItemStack itemStack, TileEntity requester, int slot)
    {
        if (members != null && !members.isEmpty())
        {
            List<List<ISortingInventory>> sortingList = createSortingList(requester);
            for (List<ISortingInventory> list : sortingList)
            {
                for (ISortingInventory inventory : list)
                {
                    if (inventory.getFilter().passesFilter(itemStack))
                    {
                        if (inventory == requester)
                        {
                            return itemStack;
                        }
                        else
                        {
                            itemStack = inventory.putInInventory(itemStack);
                            if (itemStack == null || itemStack.stackSize == 0)
                            {
                                return null;
                            }
                        }
                    }
                }
            }
        }
        return itemStack;
    }

    private List<List<ISortingInventory>> createSortingList(TileEntity requester)
    {
        List<List<ISortingInventory>> list = new ArrayList<List<ISortingInventory>>();
        for (ISortingInventory.Priority ignored : ISortingInventory.Priority.values())
        {
            list.add(new ArrayList<ISortingInventory>());
        }

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
                ISortingInventory filteringInventory = (ISortingInventory) filteringMember.getOwner();

                if (filteringInventory == requester)
                {
                    list.get(filteringInventory.getPriority().ordinal()).add(0, filteringInventory);
                }
                else
                {
                    list.get(filteringInventory.getPriority().ordinal()).add(filteringInventory);
                }
            }
        }
        return list;
    }
}
