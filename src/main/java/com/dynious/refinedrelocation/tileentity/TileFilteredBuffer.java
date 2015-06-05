package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IMultiFilterTile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileFilteredBuffer extends TileBuffer implements IMultiFilterTile
{
    private IFilterGUI filter = APIUtils.createStandardFilter(this);

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
    public TileEntity getTileEntity()
    {
        return this;
    }

    @Override
    public void onFilterChanged()
    {
        this.markDirty();
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
