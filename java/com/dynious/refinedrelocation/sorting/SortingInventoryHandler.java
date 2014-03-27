package com.dynious.refinedrelocation.sorting;

import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class SortingInventoryHandler extends SortingMemberHandler implements ISortingInventoryHandler
{
    protected ISortingInventory inventory;

    protected List<EntityPlayer> crafters = new ArrayList<EntityPlayer>();

    public SortingInventoryHandler(TileEntity owner)
    {
        super(owner);
        this.inventory = (ISortingInventory) owner;
    }

    /**
     * Forcibly sets an ItemStack to the slotIndex (Only used client side)
     *
     * @param itemStack The stack to add
     * @param slotIndex The slot index to add the ItemStack in
     */
    public final void putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        if (slotIndex >= 0 && slotIndex < inventory.getInventory().length)
            inventory.getInventory()[slotIndex] = itemStack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public final void setInventorySlotContents(int par1, ItemStack itemStack)
    {
        if (owner.getWorldObj().isRemote)
        {
            return;
        }

        if (itemStack == null || itemStack.stackSize == 0)
        {
            inventory.getInventory()[par1] = itemStack;
            inventory.markDirty();
        }
        else
        {
            itemStack = getLeader().filterStackToGroup(itemStack, this.owner);
        }


        if (itemStack != null && itemStack.stackSize != 0)
        {
            inventory.getInventory()[par1] = itemStack;
            inventory.markDirty();
        }

        syncInventory();
    }

    private void syncInventory()
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
