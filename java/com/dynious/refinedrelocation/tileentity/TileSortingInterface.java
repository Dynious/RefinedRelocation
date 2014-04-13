package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.ISortingMember;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.helper.IOHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileSortingInterface extends TileSortingConnector implements ISortingInventory, IFilterTileGUI
{
    private ISortingInventoryHandler sortingHandler = APIUtils.createSortingInventoryHandler(this);
    private IFilterGUI filter = APIUtils.createStandardFilter();

    @Override
    public ISortingInventoryHandler getSortingHandler()
    {
        return sortingHandler;
    }

    @Override
    public ItemStack[] getInventory()
    {
        return new ItemStack[0];
    }

    @Override
    public ItemStack putInInventory(ItemStack itemStack)
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tile = DirectionHelper.getTileAtSide(worldObj, xCoord, yCoord, zCoord, direction);
            if (tile != null && !(tile instanceof ISortingMember))
            {
                itemStack = IOHelper.insert(tile, itemStack, direction.getOpposite());
                if (itemStack == null || itemStack.stackSize == 0)
                    return null;
            }
        }
        return itemStack;
    }

    @Override
    public Priority getPriority()
    {
        return Priority.HIGH;
    }

    @Override
    public IFilterGUI getFilter()
    {
        return filter;
    }

    public void onTileDestroyed()
    {
        sortingHandler.onTileDestroyed();
    }

    /*
    Fake Inventory
     */

    @Override
    public int getSizeInventory()
    {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {

    }

    @Override
    public String getInvName()
    {
        return null;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return false;
    }

    @Override
    public void openChest()
    {

    }

    @Override
    public void closeChest()
    {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }
}
