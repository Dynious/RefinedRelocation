package com.dynious.refinedrelocation.api.filter;

import com.dynious.refinedrelocation.grid.filter.AbstractFilter;
import com.dynious.refinedrelocation.grid.filter.CustomUserFilter;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface IFilterGUI extends IFilter
{
    boolean isBlacklisting();

    void setBlacklists(boolean blacklists);

    List<String> getWAILAInformation(NBTTagCompound compound);

    void filterChanged();

    void writeToNBT(NBTTagCompound compound);

    void readFromNBT(NBTTagCompound compound);

    int getFilterCount();

    AbstractFilter getFilterAtIndex(int index);

    void setFilterType(int filterIndex, int filterType);

    boolean isDirty();

    void markDirty(boolean dirty);
}
