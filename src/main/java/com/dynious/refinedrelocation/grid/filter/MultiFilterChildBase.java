package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class MultiFilterChildBase implements IMultiFilterChild
{
    private IMultiFilter parentFilter;
    private int filterIndex;
    private boolean isDirty;

    @Override
    public void setParentFilter(IMultiFilter parentFilter, int filterIndex)
    {
        this.parentFilter = parentFilter;
        this.filterIndex = filterIndex;
    }

    @Override
    public IMultiFilter getParentFilter()
    {
        return parentFilter;
    }

    @Override
    public int getFilterIndex()
    {
        return filterIndex;
    }

    @Override
    public boolean canFilterBeUsedOnTile(TileEntity tile)
    {
        return true;
    }

    @Override
    public void setFilterString(int optionId, String value)
    {
    }

    @Override
    public void setFilterBoolean(int optionId, boolean value)
    {
    }

    @Override
    public void setFilterBooleanArray(int optionId, boolean[] values)
    {
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
    }

    @Override
    public void markDirty(boolean isDirty)
    {
        this.isDirty = isDirty;
    }

    @Override
    public boolean isDirty()
    {
        return isDirty;
    }

    @Override
    public void sendUpdate(EntityPlayerMP playerMP)
    {
    }
}
