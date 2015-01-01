package com.dynious.refinedrelocation.grid;

import com.dynious.refinedrelocation.api.filter.IFilterModule;
import net.minecraft.item.ItemStack;

public class Filter
{
    public IFilterModule[] filters = new IFilterModule[4];
    private boolean blacklists = false;

    public boolean passesFilter(ItemStack stack)
    {
        for (IFilterModule filter : filters)
        {
            if (filter != null)
            {
                if (filter.matchesFilter(stack))
                    return isBlacklisting();
            }
        }
        return !isBlacklisting();
    }

    public boolean isBlacklisting()
    {
        return blacklists;
    }

    public void setBlacklists(boolean blacklists)
    {
        this.blacklists = blacklists;
        //tile.onFilterChanged();
    }
}
