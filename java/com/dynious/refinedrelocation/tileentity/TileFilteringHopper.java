package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.Filter;
import com.dynious.refinedrelocation.api.IFilterTile;
import com.dynious.refinedrelocation.lib.Names;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;

public class TileFilteringHopper extends TileEntityHopper implements IFilterTile
{
    private Filter filter = new Filter();

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return super.isItemValidForSlot(par1, par2ItemStack) && filter.passesFilter(par2ItemStack);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        filter.writeToNBT(par1NBTTagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        filter.readFromNBT(par1NBTTagCompound);
    }

    @Override
    public Filter getFilter()
    {
        return filter;
    }
    
    @Override
    public String getInvName()
    {
        return this.hasCustomInventoryName() ? super.getInventoryName() : "tile." + Names.filteringHopper + ".name";
    }
}
