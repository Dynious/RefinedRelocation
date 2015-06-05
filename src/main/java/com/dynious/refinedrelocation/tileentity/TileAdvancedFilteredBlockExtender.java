package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileAdvancedFilteredBlockExtender extends TileBlockExtender implements IAdvancedFilteredTile
{
    public boolean restrictExtraction = false;
    private boolean spreadItems = false;
    private byte[] insertDirection = {1, 1, 1, 1, 1, 1, 1};
    private int bestSlot;
    private boolean shouldUpdateBestSlot = true;
    private int lastSlotSide;
    private ItemStack lastStack;
    private IFilterGUI filter = APIUtils.createStandardFilter(this);
    private byte maxStackSize = 64;

    @Override
    public boolean getRestrictExtraction()
    {
        return restrictExtraction;
    }

    @Override
    public void setRestrictionExtraction(boolean restrict)
    {
        restrictExtraction = restrict;
    }

    public byte[] getInsertDirections()
    {
        return insertDirection;
    }

    @Override
    public byte[] getInsertDirection()
    {
        return insertDirection;
    }

    public void setInsertDirection(int from, int value)
    {
        int numDirs = ForgeDirection.VALID_DIRECTIONS.length;
        value = (value % numDirs + numDirs) % numDirs;
        insertDirection[from] = (byte) value;
    }

    public void setConnectedSide(int connectedSide)
    {
        super.setConnectedSide(connectedSide);
        if (connectedDirection != ForgeDirection.UNKNOWN)
        {
            for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
            {
                insertDirection[i] = (byte) connectedDirection.getOpposite().ordinal();
            }
        }
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (spreadItems)
        {
            if (shouldUpdateBestSlot || lastSlotSide != i2 || !ItemStackHelper.areItemStacksEqual(itemStack, lastStack))
            {
                updateBestSlot(i2, itemStack);
                shouldUpdateBestSlot = false;
            }
            if (i != bestSlot || !super.canInsertItem(bestSlot, itemStack, i2))
            {
                return false;
            }
            shouldUpdateBestSlot = true;
            return filter.passesFilter(itemStack);
        }
        else
        {
            return super.canInsertItem(i, itemStack, i2) && filter.passesFilter(itemStack);
        }
    }

    private void updateBestSlot(int side, ItemStack itemStack)
    {
        int bestSize = Integer.MAX_VALUE;
        int[] invAccessibleSlots = getAccessibleSlotsFromSide(side);

        for (int slot : invAccessibleSlots)
        {
            ItemStack stack = getStackInSlot(slot);
            if (!super.canInsertItem(slot, itemStack, side))
            {
                continue;
            }
            if (stack == null)
            {
                bestSlot = slot;
                break;
            }
            if (ItemStackHelper.areItemStacksEqual(itemStack, stack) && stack.stackSize < bestSize)
            {
                bestSlot = slot;
                bestSize = stack.stackSize;
            }
        }
        lastSlotSide = side;
        lastStack = itemStack;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int i2)
    {
        return (super.canExtractItem(i, itemStack, i2) && !(restrictExtraction && filter.passesFilter(itemStack)));
    }

    @Override
    public byte getMaxStackSize()
    {
        return maxStackSize;
    }

    @Override
    public void setMaxStackSize(byte maxStackSize)
    {
        this.maxStackSize = maxStackSize;
    }

    @Override
    public boolean getSpreadItems()
    {
        return spreadItems;
    }

    @Override
    public void setSpreadItems(boolean spreadItems)
    {
        this.spreadItems = spreadItems;
    }

    @Override
    public int getInventoryStackLimit()
    {
        if (getInventory() != null)
        {
            return Math.min(super.getInventoryStackLimit(), maxStackSize);
        }
        return maxStackSize;
    }

    @Override
    public ForgeDirection getInputSide(ForgeDirection side)
    {
        return ForgeDirection.getOrientation(insertDirection[side.ordinal()]);
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
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        restrictExtraction = compound.getBoolean("restrictExtraction");
        spreadItems = compound.getBoolean("spreadItems");
        insertDirection = compound.getByteArray("insertDirection");
        maxStackSize = compound.getByte("maxStackSize");
        filter.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("restrictExtraction", restrictExtraction);
        compound.setBoolean("spreadItems", spreadItems);
        compound.setByteArray("insertDirection", insertDirection);
        compound.setByte("maxStackSize", maxStackSize);
        filter.writeToNBT(compound);
    }
}
