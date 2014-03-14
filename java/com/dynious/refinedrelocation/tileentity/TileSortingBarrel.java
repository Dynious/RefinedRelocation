package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.*;
import com.dynious.refinedrelocation.mods.BarrelFilter;
import com.dynious.refinedrelocation.mods.BarrelSortingInventoryHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileSortingBarrel extends TileEntityBarrel implements ISortingInventory
{
    public boolean isFirstRun = true;

    private BarrelFilter filter = new BarrelFilter(this);

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
    public ItemStack[] getInventory()
    {
        return null;
    }

    @Override
    public IFilter getFilter()
    {
        return filter;
    }

    @Override
    public Priority getPriority()
    {
        return Priority.HIGH;
    }

    @Override
    public SortingInventoryHandler getSortingHandler()
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
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
    }
}
