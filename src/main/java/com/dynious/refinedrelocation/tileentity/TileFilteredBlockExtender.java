package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileFilteredBlockExtender extends TileBlockExtender implements IFilterTileGUI
{
    private IFilterGUI filter = APIUtils.createStandardFilter();

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        return super.canInsertItem(i, itemStack, connectedDirection.getOpposite().ordinal()) && filter.passesFilter(itemStack);
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
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        filter.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        filter.readFromNBT(compound);
    }
}
