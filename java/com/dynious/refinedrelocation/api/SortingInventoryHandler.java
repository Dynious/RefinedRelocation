package com.dynious.refinedrelocation.api;

import com.dynious.refinedrelocation.helper.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class SortingInventoryHandler extends SortingMemberHandler
{
    protected ISortingInventory inventory;

    protected List<EntityPlayer> crafters = new ArrayList<EntityPlayer>();

    public SortingInventoryHandler(TileEntity owner)
    {
        super(owner);
        this.inventory = (ISortingInventory) owner;
    }

    /**
     * Tries to put an ItemStack into its Inventory
     *
     * @param itemStack The stack that should be put in the inventory
     * @return The remaining ItemStack after trying to put the ItemStack in the Inventory
     */
    public ItemStack putInInventory(ItemStack itemStack)
    {
        for (int slot = 0; slot < inventory.getSizeInventory() && itemStack != null && itemStack.stackSize > 0; ++slot)
        {
            if (inventory.isItemValidForSlot(slot, itemStack))
            {
                ItemStack itemstack1 = inventory.getStackInSlot(slot);

                if (itemstack1 == null)
                {
                    int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                    if (max >= itemStack.stackSize)
                    {
                        inventory.getInventory()[slot] = itemStack;

                        inventory.markDirty();
                        itemStack = null;
                    }
                    else
                    {
                        inventory.getInventory()[slot] = itemStack.splitStack(max);
                    }
                }
                else if (ItemStackHelper.areItemStacksEqual(itemstack1, itemStack))
                {
                    int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
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

    /**
     * Forcibly sets an ItemStack to the slotIndex (Only used client side)
     *
     * @param itemStack
     * @param slotIndex
     */
    public final void putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        if (slotIndex >= 0 && slotIndex < inventory.getInventory().length)
            inventory.getInventory()[slotIndex] = itemStack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public final void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        if (owner.getWorldObj().isRemote)
        {
            return;
        }

        if (par2ItemStack == null || (!inventory.getFilter().blacklists && inventory.getFilter().passesFilter(par2ItemStack)))
        {
            inventory.getInventory()[par1] = par2ItemStack;
            inventory.markDirty();
        }
        else
        {
            ItemStack filteredStack = getLeader().filterStackToGroup(par2ItemStack, this);
            if (filteredStack != null)
            {
                inventory.getInventory()[par1] = par2ItemStack;
                inventory.markDirty();
            }
        }
        syncInventory();
    }

    public final void syncInventory()
    {
        for (EntityPlayer player : crafters)
        {
            player.openContainer.detectAndSendChanges();
        }
    }

    /**
     * Should be called by the constructor of the Container of this tile, used to sync Inventory contents
     *
     * @param player The player opening the container
     */
    public final void addCrafter(EntityPlayer player)
    {
        crafters.add(player);
    }

    /**
     * Should be called by onContainerClosed(...) of the Container of this this tile, used to stop syncing Inventory contents
     *
     * @param player The player closing the container
     */
    public final void removeCrafter(EntityPlayer player)
    {
        crafters.remove(player);
    }
}
