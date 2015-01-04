package com.dynious.refinedrelocation.grid;

import com.dynious.refinedrelocation.api.filter.IFilterModule;
import com.dynious.refinedrelocation.api.tileentity.INewFilterTile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class Filter
{
    public IFilterModule[] filters = new IFilterModule[4];
    private INewFilterTile tile;
    private boolean blacklists = false;

    public Filter(INewFilterTile tile)
    {
        this.tile = tile;
    }

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
        tile.onFilterChanged();
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagList nbttaglist = compound.getTagList("filters", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte place = nbttagcompound1.getByte("place");
            IFilterModule module = FilterModuleRegistry.getModule(nbttagcompound1.getString("clazzIdentifier"));
            if (module != null)
            {
                module.init(tile);
                filters[place] = module;
                filters[place].readFromNBT(nbttagcompound1);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < filters.length; i++)
        {
            if (filters[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setString("clazzIdentifier", FilterModuleRegistry.getIdentifier(filters[i].getClass()));
                nbttagcompound1.setByte("place", (byte) i);
                filters[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        compound.setTag("filters", nbttaglist);
    }
}
