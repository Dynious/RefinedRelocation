package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.pahimar.ee3.inventory.ContainerAlchemicalChest;
import com.pahimar.ee3.tileentity.TileAlchemicalChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Field;

public class TileSortingAlchemicalChest extends TileAlchemicalChest implements ISortingInventory, IFilterTileGUI
{
    public boolean isFirstRun = true;
    private IFilterGUI filter = APIUtils.createStandardFilter();
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
            worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, newChest);

            return true;
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
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
