package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.api.tileentity.IMultiFilterTile;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.dynious.refinedrelocation.lib.Names;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;

public class TileFilteringHopper extends TileEntityHopper implements IMultiFilterTile, ISidedInventory
{
    private static int[] accessibleSlots = new int[]{0, 1, 2, 3, 4};
    private IMultiFilter filter = APIUtils.createStandardFilter(this);
    private ItemStack checkedItemStack;
    private boolean passedItemStackFilter;

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return accessibleSlots;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j)
    {
        if (!ItemStackHelper.areItemStacksEqual(itemstack, checkedItemStack))
        {
            checkedItemStack = itemstack;
            passedItemStackFilter = filter.passesFilter(itemstack);
        }
        return passedItemStackFilter;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        return true;
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
    public IMultiFilter getFilter()
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
        checkedItemStack = null;
        this.markDirty();
    }

    @Override
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? super.getInventoryName() : "tile." + Names.filteringHopper + ".name";
    }
}
