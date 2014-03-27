package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.lib.Names;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;

public class TileFilteringHopper extends TileEntityHopper implements IFilterTileGUI
{
    private IFilterGUI filter = APIUtils.createStandardFilter();

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
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? super.getInventoryName() : "tile." + Names.filteringHopper + ".name";
    }
}
