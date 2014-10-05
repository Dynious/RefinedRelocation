package com.dynious.refinedrelocation.helper;

import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import cofh.api.transport.IItemDuct;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class IOHelper
{
    public static ItemStack extract(IInventory inventory, ForgeDirection direction)
    {
        if (inventory instanceof ISidedInventory)
        {
            ISidedInventory iSidedInventory = (ISidedInventory)inventory;
            int[] accessibleSlotsFromSide = iSidedInventory.getAccessibleSlotsFromSide(direction.ordinal());

            for (int anAccessibleSlotsFromSide : accessibleSlotsFromSide)
            {
                ItemStack stack = extract(inventory, direction, anAccessibleSlotsFromSide);
                if (stack != null)
                    return stack;
            }
        }
        else
        {
            int j = inventory.getSizeInventory();

            for (int slot = 0; slot < j; ++slot)
            {
                ItemStack stack = extract(inventory, direction, slot);
                if (stack != null)
                    return stack;
            }
        }
        return null;
    }

    public static ItemStack extract(IInventory inventory, ForgeDirection direction, int slot)
    {
        ItemStack itemstack = inventory.getStackInSlot(slot);

        if (itemstack != null && canExtractItemFromInventory(inventory, itemstack, slot, direction.ordinal()))
        {
            inventory.setInventorySlotContents(slot, null);
            return itemstack;
        }
        return null;
    }

    public static ItemStack insert(TileEntity tile, ItemStack itemStack, ForgeDirection side, boolean simulate)
    {
        if (tile instanceof IRelocator)
        {
            return ((IRelocator) tile).insert(itemStack, side.ordinal(), simulate);
        }
        else if (Mods.IS_COFH_TRANSPORT_API_LOADED && tile instanceof IItemDuct)
        {
            if (simulate)
            {
                return null;
            }
            return ((IItemDuct) tile).insertItem(side, itemStack);
        }
        if (Mods.IS_BC_TRANSPORT_API_LOADED && tile instanceof IPipeTile)
        {
            IPipeTile pipe = (IPipeTile) tile;
            if (pipe.isPipeConnected(side))
            {
                int size = pipe.injectItem(itemStack, !simulate, side);
                itemStack.stackSize -= size;
                if (itemStack.stackSize == 0)
                {
                    return null;
                }
                return itemStack;
            }
        }
        else if (tile instanceof IInventory)
        {
            return insert((IInventory) tile, itemStack, side.ordinal(), simulate);
        }
        return itemStack;
    }

    public static ItemStack insert(IInventory inventory, ItemStack itemStack, int side, boolean simulate)
    {
        ItemStack stackInSlot;
        int emptySlot = -1;

        if (inventory instanceof ISidedInventory && side > -1)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventory;
            int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

            for (int j = 0; j < aint.length && itemStack != null && itemStack.stackSize > 0; ++j)
            {
                stackInSlot = inventory.getStackInSlot(aint[j]);
                if (canInsertItemToInventory(inventory, stackInSlot, aint[j], side))
                {
                    if (stackInSlot == null)
                    {
                        if (simulate)
                            return null;

                        if (emptySlot == -1)
                            emptySlot = aint[j];
                        continue;
                    }
                    itemStack = insert(inventory, itemStack, stackInSlot, aint[j], simulate);
                }
            }
        }
        else
        {
            int invSize = inventory.getSizeInventory();

            for (int slot = 0; slot < invSize && itemStack != null && itemStack.stackSize > 0; ++slot)
            {
                stackInSlot = inventory.getStackInSlot(slot);
                if (canInsertItemToInventory(inventory, stackInSlot, slot, side))
                {
                    if (stackInSlot == null)
                    {
                        if (simulate)
                            return null;

                        if (emptySlot == -1)
                            emptySlot = slot;
                        continue;
                    }
                    itemStack = insert(inventory, itemStack, stackInSlot, slot, simulate);
                }
            }
        }

        if (itemStack != null && itemStack.stackSize != 0 && emptySlot != -1)
        {
            stackInSlot = inventory.getStackInSlot(emptySlot);
            itemStack = insert(inventory, itemStack, stackInSlot, emptySlot, simulate);
        }

        if (itemStack != null && itemStack.stackSize == 0)
        {
            itemStack = null;
        }

        return itemStack;
    }

    public static ItemStack insert(IInventory inventory, ItemStack itemStack, ItemStack stackInSlot, int slot, boolean simulate)
    {
        boolean flag = false;

        if (stackInSlot == null)
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
        else if (ItemStackHelper.areItemStacksEqual(stackInSlot, itemStack))
        {
            int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
            if (max > stackInSlot.stackSize)
            {
                int l = Math.min(itemStack.stackSize, max - stackInSlot.stackSize);
                itemStack.stackSize -= l;
                if (!simulate)
                {
                    stackInSlot.stackSize += l;
                    flag = l > 0;
                }
            }
        }
        if (flag)
        {
            inventory.markDirty();
        }

        return itemStack;
    }

    public static boolean canInsertItemToInventory(IInventory inventory, ItemStack itemStack, int slot, int side)
    {
        return inventory.isItemValidForSlot(slot, itemStack) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canInsertItem(slot, itemStack, side));
    }

    public static boolean canExtractItemFromInventory(IInventory inventory, ItemStack itemStack, int slot, int side)
    {
        return !(inventory instanceof ISidedInventory) || ((ISidedInventory)inventory).canExtractItem(slot, itemStack, side);
    }

    public static void dropInventory(World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (!(tileEntity instanceof IInventory))
        {
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.stackSize > 0)
            {
                spawnItemInWorld(world, itemStack, x, y, z);
            }
        }
    }

    public static void spawnItemInWorld(World world, ItemStack itemStack, double x, double y, double z)
    {
        if (world.isRemote) return;
        float dX = world.rand.nextFloat() * 0.8F + 0.1F;
        float dY = world.rand.nextFloat() * 0.8F + 0.1F;
        float dZ = world.rand.nextFloat() * 0.8F + 0.1F;

        EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.getItem(), itemStack.stackSize, itemStack.getItemDamage()));

        if (itemStack.hasTagCompound())
        {
            entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
        }

        float factor = 0.05F;
        entityItem.motionX = world.rand.nextGaussian() * factor;
        entityItem.motionY = world.rand.nextGaussian() * factor + 0.2F;
        entityItem.motionZ = world.rand.nextGaussian() * factor;
        world.spawnEntityInWorld(entityItem);
        itemStack.stackSize = 0;
    }

    public static boolean canInterfaceWith(TileEntity tile, ForgeDirection side)
    {
        if (tile instanceof IRelocator)
        {
            return ((IRelocator)tile).connectsToSide(side.ordinal());
        }
        else if (Mods.IS_COFH_TRANSPORT_API_LOADED && tile instanceof IItemDuct)
        {
            return true;
        }
        else if (Mods.IS_BC_TRANSPORT_API_LOADED && tile instanceof IPipeTile)
        {
            if (((IPipeTile)tile).getPipeType() == IPipeTile.PipeType.ITEM)
                return true;
        }
        else if (Mods.IS_BC_TRANSPORT_API_LOADED && tile instanceof IPipeConnection)
        {
            return ((IPipeConnection)tile).overridePipeConnection(IPipeTile.PipeType.ITEM, side) != IPipeConnection.ConnectOverride.DISCONNECT;
        }
        else if (tile instanceof IInventory)
        {
            return !(tile instanceof ISidedInventory) || ((ISidedInventory) tile).getAccessibleSlotsFromSide(side.ordinal()).length > 0;
        }
        return false;
    }
}
