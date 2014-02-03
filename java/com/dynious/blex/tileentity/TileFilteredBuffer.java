package com.dynious.blex.tileentity;

import com.dynious.blex.config.Filter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileFilteredBuffer extends TileBuffer implements IFilterTile
{
    private boolean blacklist = true;
    private Filter filter = new Filter();

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side)
    {
        return (blacklist ? !filter.passesFilter(itemstack) : filter.passesFilter(itemstack)) && super.canInsertItem(slot, itemstack, side);
    }

    @Override
    public Filter getFilter()
    {
        return filter;
    }

    @Override
    public boolean getBlackList()
    {
        return blacklist;
    }

    @Override
    public void setBlackList(boolean value)
    {
        blacklist = value;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        blacklist = compound.getBoolean("blacklist");
        filter.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("blacklist", blacklist);
        filter.writeToNBT(compound);
    }
}
