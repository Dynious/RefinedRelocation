package com.dynious.blex.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

public class TileAdvancedBlockExtender extends TileBlockExtender
{
    private boolean spreadItems = false;
    private byte[] insertDirection;
    private int bestSlot;
    private boolean shouldUpdateBestSlot = true;
    private int lastSlotSide;

    public TileAdvancedBlockExtender()
    {
        insertDirection = new byte[ForgeDirection.values().length];
        for (int i : insertDirection)
        {
            insertDirection[i] = (byte)i;
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack)
    {
        if (spreadItems)
        {
            if (lastSlotSide != i || shouldUpdateBestSlot)
            {
                updateBestSlot(i);
                shouldUpdateBestSlot = false;
            }
            if (!super.isItemValidForSlot(bestSlot, itemStack) || i != bestSlot)
            {
                return false;
            }
            shouldUpdateBestSlot = true;
            return true;
        }
        else
        {
            return super.isItemValidForSlot(i, itemStack);
        }
    }

    private void updateBestSlot(int side)
    {
        int[] slots = super.getAccessibleSlotsFromSide(insertDirection[side]);
        int bestSize = Integer.MAX_VALUE;
        for (int slot : slots)
        {
            ItemStack stack = getStackInSlot(slot);
            if (stack == null)
            {
                bestSlot = slot;
                break;
            }
            if (stack.stackSize < bestSize)
            {
                bestSlot = slot;
                bestSize = stack.stackSize;
            }
        }
        lastSlotSide = side;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                return ((ISidedInventory)inventory).getAccessibleSlotsFromSide(insertDirection[i]);
            }
            return accessibleSlots;
        }
        return new int[0];
    }
}
