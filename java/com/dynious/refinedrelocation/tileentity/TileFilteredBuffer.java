package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.Filter;
import com.dynious.refinedrelocation.api.IFilterTile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileFilteredBuffer extends TileBuffer implements IFilterTile
{
    private Filter filter = new Filter();

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side)
    {
        return filter.passesFilter(itemstack) && super.canInsertItem(slot, itemstack, side);
    }

    @Override
    public Filter getFilter()
    {
        return filter;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        filter.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        filter.writeToNBT(compound);
    }
}
