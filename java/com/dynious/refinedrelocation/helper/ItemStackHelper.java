package com.dynious.refinedrelocation.helper;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class ItemStackHelper
{
    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2)
    {
        return itemStack1 == null && itemStack2 == null || (!(itemStack1 == null || itemStack2 == null) && (itemStack1.itemID == itemStack2.itemID && (itemStack1.getItemDamage() == itemStack2.getItemDamage() && (!(itemStack1.stackTagCompound == null && itemStack2.stackTagCompound != null) && (itemStack1.stackTagCompound == null || itemStack1.stackTagCompound.equals(itemStack2.stackTagCompound))))));
    }

    public static ItemStack simulateInsertion(IInventory inventory, ItemStack itemStack, int side)
    {
        if (inventory instanceof ISidedInventory && side > -1)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventory;
            int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

            for (int j = 0; j < aint.length && itemStack != null && itemStack.stackSize > 0; ++j)
            {
                itemStack = simulateInsertion(inventory, itemStack, aint[j], side);
            }
        }
        else
        {
            int k = inventory.getSizeInventory();

            for (int l = 0; l < k && itemStack != null && itemStack.stackSize > 0; ++l)
            {
                itemStack = simulateInsertion(inventory, itemStack, l, side);
            }
        }

        if (itemStack != null && itemStack.stackSize == 0)
        {
            itemStack = null;
        }

        return itemStack;
    }

    private static ItemStack simulateInsertion(IInventory inventory, ItemStack itemStack, int slot, int side)
    {
        ItemStack itemstack1 = inventory.getStackInSlot(slot);

        if (canInsertItemToInventory(inventory, itemStack, slot, side))
        {
            if (itemstack1 == null)
            {
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max >= itemStack.stackSize)
                {
                    itemStack = null;
                }
                else
                {
                    itemStack.splitStack(max);
                }
            }
            else if (areItemStacksEqual(itemstack1, itemStack))
            {
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max > itemstack1.stackSize)
                {
                    int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                    itemStack.stackSize -= l;
                }
            }
        }

        return itemStack;
    }

    private static boolean canInsertItemToInventory(IInventory inventory, ItemStack itemStack, int slot, int side)
    {
        return inventory.isItemValidForSlot(slot, itemStack) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canInsertItem(slot, itemStack, side));
    }
}
