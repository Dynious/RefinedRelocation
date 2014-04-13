package com.dynious.refinedrelocation.helper;

import buildcraft.api.transport.IPipeTile;
import cofh.api.transport.IItemConduit;
import cpw.mods.fml.common.Loader;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class IOHelper
{
    public static ItemStack insert(TileEntity tile, ItemStack itemStack, ForgeDirection side)
    {
        if (Loader.isModLoaded("CoFHCore") && tile instanceof IItemConduit)
        {
            return ((IItemConduit) tile).insertItem(side, itemStack);
        }
        else if (Loader.isModLoaded("BuildCraft|Transport") && tile instanceof IPipeTile)
        {
            IPipeTile pipe = (IPipeTile) tile;
            if (pipe.isPipeConnected(side))
            {
                int size = pipe.injectItem(itemStack, true, side);
                itemStack.stackSize -= size;
                return itemStack;
            }
        }
        else if (tile instanceof IInventory)
        {
            return insert((IInventory) tile, itemStack, side.ordinal(), false);
        }
        return itemStack;
    }

    public static ItemStack insert(IInventory inventory, ItemStack itemStack, int side, boolean simulate)
    {
        if (inventory instanceof ISidedInventory && side > -1)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventory;
            int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

            for (int j = 0; j < aint.length && itemStack != null && itemStack.stackSize > 0; ++j)
            {
                itemStack = insert(inventory, itemStack, aint[j], side, simulate);
            }
        }
        else
        {
            int k = inventory.getSizeInventory();

            for (int l = 0; l < k && itemStack != null && itemStack.stackSize > 0; ++l)
            {
                itemStack = insert(inventory, itemStack, l, side, simulate);
            }
        }

        if (itemStack != null && itemStack.stackSize == 0)
        {
            itemStack = null;
        }

        return itemStack;
    }

    public static ItemStack insert(IInventory inventory, ItemStack itemStack, int slot, int side, boolean simulate)
    {
        ItemStack itemstack1 = inventory.getStackInSlot(slot);

        if (canInsertItemToInventory(inventory, itemStack, slot, side))
        {
            boolean flag = false;

            if (itemstack1 == null)
            {
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max >= itemStack.stackSize)
                {
                    if (!simulate)
                    {
                        inventory.setInventorySlotContents(slot, itemStack);
                        flag = true;
                    }
                    itemStack = null;
                }
                else
                {
                    if (!simulate)
                    {
                        inventory.setInventorySlotContents(slot, itemStack.splitStack(max));
                        flag = true;
                    }
                    else
                    {
                        itemStack.splitStack(max);
                    }
                }
            }
            else if (ItemStackHelper.areItemStacksEqual(itemstack1, itemStack))
            {
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max > itemstack1.stackSize)
                {
                    int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                    itemStack.stackSize -= l;
                    if (!simulate)
                    {
                        itemstack1.stackSize += l;
                        flag = l > 0;
                    }
                }
            }
            if (flag)
            {
                inventory.onInventoryChanged();
            }
        }

        return itemStack;
    }

    private static boolean canInsertItemToInventory(IInventory inventory, ItemStack itemStack, int slot, int side)
    {
        return inventory.isItemValidForSlot(slot, itemStack) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canInsertItem(slot, itemStack, side));
    }
}
