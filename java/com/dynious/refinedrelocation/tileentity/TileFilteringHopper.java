package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.FilterStandard;
import com.dynious.refinedrelocation.api.IFilterGUI;
import com.dynious.refinedrelocation.api.IFilterTileGUI;
import com.dynious.refinedrelocation.lib.Names;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;

public class TileFilteringHopper extends TileEntityHopper implements IFilterTileGUI
{
    private FilterStandard filter = new FilterStandard();

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
    public IFilterGUI getFilter()
    {
        return filter;
    }
    
    @Override
    public String getInvName()
    {
        return this.isInvNameLocalized() ? super.getInvName() : "tile." + Names.filteringHopper + ".name";
    }
}
