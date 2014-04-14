package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.ISortingMember;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileSortingInterface extends TileSortingConnector implements ISortingInventory, IFilterTileGUI
{
    private ISortingInventoryHandler sortingHandler = APIUtils.createSortingInventoryHandler(this);
    private IFilterGUI filter = APIUtils.createStandardFilter();
    public ItemStack[] bufferInventory = new ItemStack[1];
    private int counter;

    @Override
    public ISortingInventoryHandler getSortingHandler()
    {
        return sortingHandler;
    }

    @Override
    public ItemStack[] getInventory()
    {
        return bufferInventory;
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
    public final boolean putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        itemStack = putInInventory(itemStack);
        if (itemStack != null)
        {
            bufferInventory[0] = itemStack;
        }
        return true;
    }

    @Override
    public Priority getPriority()
    {
        return filter.isBlacklisting() ? Priority.LOW : Priority.NORMAL;
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

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (bufferInventory[0] != null)
        {
            counter++;
            if (counter % 22 == 0)
            {
                ItemStack stack = bufferInventory[0].copy();
                bufferInventory[0] = null;
                sortingHandler.setInventorySlotContents(0, stack);
            }
        }
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return bufferInventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount)
    {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null)
        {
            if (itemStack.stackSize <= decrementAmount)
            {
                setInventorySlotContents(slotIndex, null);
            }
            else
            {
                itemStack = itemStack.splitStack(decrementAmount);
                if (itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slotIndex, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex)
    {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null)
        {
            setInventorySlotContents(slotIndex, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        sortingHandler.setInventorySlotContents(slotIndex, itemStack);
    }

    @Override
    public String getInvName()
    {
        return Names.sortingInterface;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
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
        return bufferInventory[0] == null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        filter.readFromNBT(compound);
        this.bufferInventory[0] = ItemStack.loadItemStackFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        filter.writeToNBT(compound);
        if (bufferInventory[0] != null)
        {
            this.bufferInventory[0].writeToNBT(compound);
        }
    }
}
