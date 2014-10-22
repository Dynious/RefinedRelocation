package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.pahimar.ee3.tileentity.TileEntityAlchemicalChest;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.TileEntityIronChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileSortingAlchemicalChest extends TileEntityAlchemicalChest implements ISortingInventory, IFilterTileGUI
{
    public boolean isFirstRun = true;
    private IFilterGUI filter = APIUtils.createStandardFilter(this);
    private ISortingInventoryHandler sortingInventoryHandler = APIUtils.createSortingInventoryHandler(this);
    private Priority priority = Priority.NORMAL;

    public TileSortingAlchemicalChest()
    {
        super(0);
    }

    public TileSortingAlchemicalChest(int metaData)
    {
        super(metaData);
    }

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
    public final boolean putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        super.setInventorySlotContents(slotIndex, itemStack);
        return true;
    }

    @Override
    public ItemStack putInInventory(ItemStack itemStack, boolean simulate)
    {
        int emptySlot = -1;
        for (int slot = 0; slot < getSizeInventory() && itemStack != null && itemStack.stackSize > 0; ++slot)
        {
            if (isItemValidForSlot(slot, itemStack))
            {
                ItemStack itemstack1 = getStackInSlot(slot);

                if (itemstack1 == null)
                {
                    if (simulate)
                        return null;
                    if (emptySlot == -1)
                        emptySlot = slot;
                }
                else if (ItemStackHelper.areItemStacksEqual(itemstack1, itemStack))
                {
                    int max = Math.min(itemStack.getMaxStackSize(), getInventoryStackLimit());
                    if (max > itemstack1.stackSize)
                    {
                        int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                        itemStack.stackSize -= l;
                        if (!simulate)
                        {
                            itemstack1.stackSize += l;
                            markDirty();
                        }
                    }
                }
            }
        }

        if (itemStack != null && itemStack.stackSize != 0 && emptySlot != -1)
        {
            super.setInventorySlotContents(emptySlot, itemStack);
            itemStack = null;
            markDirty();
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
        this.markDirty();
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
    public void markDirty()
    {
        super.markDirty();
        getHandler().onInventoryChange();
    }

    @Override
    public ISortingInventoryHandler getHandler()
    {
        return sortingInventoryHandler;
    }

    //@Override
    public boolean upgradeChest(int upgradeMetadata)
    {
        if (upgradeMetadata > getBlockMetadata())
        {
            //If players are using this chest, don't upgrade
            if (numUsingPlayers > 0)
            {
                return false;
            }

            //Make the new Sorting Alchemical Chest TileEntity
            TileSortingAlchemicalChest newChest = (TileSortingAlchemicalChest) ModBlocks.sortingAlchemicalChest.createTileEntity(worldObj, upgradeMetadata);

            //Set the correct orientation
            newChest.setOrientation(getOrientation());

            //Copy all the ItemStacks in our new chest and delete the ItemStacks in the old chest
            for (int slot = 0; slot < getSizeInventory(); slot++)
            {
                newChest.putStackInSlot(getStackInSlot(slot), slot);
                setInventorySlotContents(slot, null);
            }

            NBTTagCompound tag = new NBTTagCompound();
            getFilter().writeToNBT(tag);
            newChest.getFilter().readFromNBT(tag);

            //Set our new metadata and TileEntity instead
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, upgradeMetadata, 3);
            worldObj.setTileEntity(xCoord, yCoord, zCoord, newChest);

            return true;
        }
        return false;
    }

    private void fixState(byte state)
    {
        ItemStack[] inventory = new ItemStack[48];
        if (state == 1)
        {
            inventory = new ItemStack[84];
        }
        else if (state == 2)
        {
            inventory = new ItemStack[117];
        }
        ReflectionHelper.setPrivateValue(TileEntityAlchemicalChest.class, this, inventory, "inventory");
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        fixState(compound.getByte("state"));
        super.readFromNBT(compound);
        filter.readFromNBT(compound);
        if (compound.hasKey("priority"))
        {
            setPriority(Priority.values()[compound.getByte("priority")]);
        }
        else
        {
            setPriority(filter.isBlacklisting() ? Priority.LOW : Priority.NORMAL);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setByte("state", state);
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
