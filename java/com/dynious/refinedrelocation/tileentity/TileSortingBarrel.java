package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.Filter;
import com.dynious.refinedrelocation.api.ISortingInventory;
import com.dynious.refinedrelocation.api.SortingInventoryHandler;
import com.dynious.refinedrelocation.api.SortingMemberHandler;
import com.dynious.refinedrelocation.mods.BarrelSortingInventoryHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileSortingBarrel extends TileEntityBarrel implements ISortingInventory
{
    public boolean isFirstRun = true;

    private Filter filter = new Filter();

    private BarrelSortingInventoryHandler sortingInventoryHandler = new BarrelSortingInventoryHandler(this);

    @Override
    public void updateEntity()
    {
        if (isFirstRun)
        {
            sortingInventoryHandler.onTileAdded();
            isFirstRun = false;
        }
        super.updateEntity();
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        sortingInventoryHandler.setInventorySlotContents(i, itemstack);
    }

    @Override
    public SortingInventoryHandler getSortingInventoryHandler()
    {
        return sortingInventoryHandler;
    }

    @Override
    public ItemStack[] getInventory()
    {
        return null;
    }

    @Override
    public Filter getFilter()
    {
        return filter;
    }

    @Override
    public SortingMemberHandler getSortingMemberHandler()
    {
        return sortingInventoryHandler;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 0 || pass == 1;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        filter.readFromNBT(nbttagcompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        filter.writeToNBT(nbttagcompound);
    }
}
