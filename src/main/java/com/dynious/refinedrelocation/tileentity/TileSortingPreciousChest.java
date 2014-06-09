package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import rebelkeithy.mods.metallurgy.machines.chests.TileEntityPreciousChest;

public class TileSortingPreciousChest extends TileEntityPreciousChest implements ISortingInventory, IFilterTileGUI
{
    public boolean isFirstRun = true;
    private IFilterGUI filter = APIUtils.createStandardFilter(this);
    private ISortingInventoryHandler sortingInventoryHandler = APIUtils.createSortingInventoryHandler(this);
    private Priority priority = Priority.NORMAL;
    private int ticksSinceSync;

    @Override
    public void updateEntity()
    {
        if (isFirstRun)
        {
            sortingInventoryHandler.onTileAdded();
            isFirstRun = false;
        }
        if (++ticksSinceSync % 20 * 4 == 0)
        {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType().blockID, 2, (Integer) ReflectionHelper.getPrivateValue(TileEntityPreciousChest.class, this, "direction"));
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType().blockID, 3, (Integer) ReflectionHelper.getPrivateValue(TileEntityPreciousChest.class, this, "type"));
        }

        super.updateEntity();
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        sortingInventoryHandler.setInventorySlotContents(i, itemstack);
    }

    @Override
    public final boolean putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        super.setInventorySlotContents(slotIndex, itemStack);
        return true;
    }

    @Override
    public ItemStack putInInventory(ItemStack itemStack)
    {
        for (int slot = 0; slot < getSizeInventory() && itemStack != null && itemStack.stackSize > 0; ++slot)
        {
            if (isItemValidForSlot(slot, itemStack))
            {
                ItemStack itemstack1 = getStackInSlot(slot);

                if (itemstack1 == null)
                {
                    int max = Math.min(itemStack.getMaxStackSize(), getInventoryStackLimit());
                    if (max >= itemStack.stackSize)
                    {
                        super.setInventorySlotContents(slot, itemStack);

                        onInventoryChanged();
                        itemStack = null;
                    }
                    else
                    {
                        super.setInventorySlotContents(slot, itemStack.splitStack(max));
                    }
                }
                else if (ItemStackHelper.areItemStacksEqual(itemstack1, itemStack))
                {
                    int max = Math.min(itemStack.getMaxStackSize(), getInventoryStackLimit());
                    if (max > itemstack1.stackSize)
                    {
                        int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                        itemStack.stackSize -= l;
                        itemstack1.stackSize += l;
                    }
                }
            }
        }
        return itemStack;
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
    public void onFilterChanged()
    {
        this.onInventoryChanged();
    }

    @Override
    public Priority getPriority()
    {
        return priority;
    }

    @Override
    public void setPriority(Priority priority)
    {
        this.priority = priority;
    }

    @Override
    public ISortingInventoryHandler getHandler()
    {
        return sortingInventoryHandler;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        filter.readFromNBT(compound);
        if (compound.hasKey("priority"))
        {
            setPriority(ISortingInventory.Priority.values()[compound.getByte("priority")]);
        }
        else
        {
            setPriority(filter.isBlacklisting() ? ISortingInventory.Priority.LOW : ISortingInventory.Priority.NORMAL);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        filter.writeToNBT(compound);
        compound.setByte("priority", (byte) priority.ordinal());
    }

    @Override
    public void invalidate()
    {
        sortingInventoryHandler.onTileRemoved();
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        sortingInventoryHandler.onTileRemoved();
        super.onChunkUnload();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 0 || pass == 1;
    }
}
