package com.dynious.refinedrelocation.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IFilterGUI extends IFilter
{
    public int getSize();

    public void setValue(int place, boolean value);

    public boolean getValue(int place);

    public String getName(int place);

    public boolean isBlacklisting();

    public void setBlacklists(boolean blacklists);

    public String getUserFilter();

    public void setUserFilter(String userFilter);

    public void writeToNBT(NBTTagCompound compound);
    public void readFromNBT(NBTTagCompound compound);
}
