package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.FilterStandard;
import com.dynious.refinedrelocation.api.IFilterGUI;
import com.dynious.refinedrelocation.api.IFilterGUITile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileFilteredBuffer extends TileBuffer implements IFilterGUITile
{
    private FilterStandard filter = new FilterStandard();

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side)
    {
        return filter.passesFilter(itemstack) && super.canInsertItem(slot, itemstack, side);
    }

    @Override
    public IFilterGUI getFilter()
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
