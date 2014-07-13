package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.helper.ItemStackHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileAdvancedBlockExtender extends TileBlockExtender implements IAdvancedTile
{
    private boolean spreadItems = false;
    private byte[] insertDirection = {1, 1, 1, 1, 1, 1, 1};
    private int bestSlot;
    private boolean shouldUpdateBestSlot = true;
    private int lastSide;
    private ItemStack lastStack;
    private byte maxStackSize = 64;

    @Override
    public byte[] getInsertDirection()
    {
        return insertDirection;
    }

    @Override
    public void setInsertDirection(int from, int value)
    {
        int numDirs = ForgeDirection.VALID_DIRECTIONS.length;
        value = (value % numDirs + numDirs) % numDirs;
        insertDirection[from] = (byte) value;
    }

    @Override
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

    public boolean getSpreadItems()
    {
        return spreadItems;
    }

    public void setSpreadItems(boolean spreadItems)
    {
        this.spreadItems = spreadItems;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (spreadItems)
        {
            if (shouldUpdateBestSlot || lastSide != i2 || !ItemStackHelper.areItemStacksEqual(itemStack, lastStack))
            {
                updateBestSlot(i2, itemStack);
                shouldUpdateBestSlot = false;
            }
            if (i != bestSlot || !super.canInsertItem(bestSlot, itemStack, i2))
            {
                return false;
            }
            shouldUpdateBestSlot = true;
            return true;
        }
        else
        {
            return super.canInsertItem(i, itemStack, i2);
        }
    }

    private void updateBestSlot(int side, ItemStack itemStack)
    {
        int bestSize = Integer.MAX_VALUE;
        int[] invAccessibleSlots = getAccessibleSlotsFromSide(side);

        for (int j = 0; j < invAccessibleSlots.length; ++j)
        {
            int slot = invAccessibleSlots[j];
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
        lastSide = side;
        lastStack = itemStack;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i)
    {
        if (getInventory() != null)
        {
            if (getInventory() instanceof ISidedInventory)
            {
                return ((ISidedInventory) getInventory()).getAccessibleSlotsFromSide(getInputSide(ForgeDirection.getOrientation(i)).ordinal());
            }
            return accessibleSlots;
        }
        return new int[0];
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
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        spreadItems = compound.getBoolean("spreadItems");
        insertDirection = compound.getByteArray("insertDirection");
        maxStackSize = compound.getByte("maxStackSize");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("spreadItems", spreadItems);
        compound.setByteArray("insertDirection", insertDirection);
        compound.setByte("maxStackSize", maxStackSize);
    }
}
