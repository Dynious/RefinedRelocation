package com.dynious.refinedrelocation.api.filter;

import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface IMultiFilter extends IFilter
{
    boolean isBlacklisting();

    void setBlacklists(boolean blacklists);

    List<String> getWAILAInformation(NBTTagCompound compound);

    void filterChanged();

    void writeToNBT(NBTTagCompound compound);

    void readFromNBT(NBTTagCompound compound);

    boolean isDirty();

    void markDirty(boolean dirty);

    int getFilterCount();

    IMultiFilterChild getFilterAtIndex(int index);

    void setFilterType(int filterIndex, String filterType);
}
