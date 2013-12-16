package com.dynious.blex.tileentity;

import com.dynious.blex.config.Filter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileFilteredBlockExtender extends TileBlockExtender
{
    public boolean blacklist = true;
    public Filter filter = new Filter();

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (!super.canInsertItem(i, itemStack, i2))
        {
            return false;
        }
        return blacklist ? !filter.passesFilter(itemStack) : filter.passesFilter(itemStack);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("blacklist", blacklist);
        filter.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        blacklist = compound.getBoolean("blacklist");
        filter.readFromNBT(compound);
    }
}
