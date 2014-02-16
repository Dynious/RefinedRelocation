package com.dynious.blex.tileentity;

import com.dynious.blex.config.Filter;
import com.dynious.blex.helper.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public abstract class TileFilteringInventory extends TileFilteringMember implements IInventory, IFilterTile
{
    protected List<EntityPlayer> crafters = new ArrayList<EntityPlayer>();
    protected ItemStack[] inventory;

    private Filter filter = new Filter();
    private boolean blacklist = true;

    public TileFilteringInventory()
    {
        inventory = new ItemStack[getSizeInventory()];
    }

    /**
     * Tries to put an ItemStack into its Inventory
     *
     * @param itemStack The stack that should be put in the inventory
     * @return The remaining ItemStack after trying to put the ItemStack in the Inventory
     */
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
                        this.inventory[slot] = itemStack;

                        this.onInventoryChanged();
                        itemStack = null;
                    }
                    else
                    {
                        this.inventory[slot] = itemStack.splitStack(max);
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
    public ItemStack filterStackToGroup(ItemStack itemStack)
    {
        itemStack = super.filterStackToGroup(itemStack);

        if (getBlackList() ? !getFilter().passesFilter(itemStack) : getFilter().passesFilter(itemStack))
        {
            itemStack = putInInventory(itemStack);
            if (itemStack == null || itemStack.stackSize == 0)
            {
                return null;
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
    public void putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        if (slotIndex >= 0 && slotIndex < inventory.length)
            this.inventory[slotIndex] = itemStack;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public abstract int getSizeInventory();

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return this.inventory[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.inventory[par1] != null)
        {
            ItemStack itemstack;

            if (this.inventory[par1].stackSize <= par2)
            {
                itemstack = this.inventory[par1];
                this.inventory[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.inventory[par1].splitStack(par2);

                if (this.inventory[par1].stackSize == 0)
                {
                    this.inventory[par1] = null;
                }

                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.inventory[par1] != null)
        {
            ItemStack itemstack = this.inventory[par1];
            this.inventory[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        if (worldObj.isRemote)
        {
            return;
        }
        if (par2ItemStack == null || getBlackList() ? !getFilter().passesFilter(par2ItemStack) : getFilter().passesFilter(par2ItemStack))
        {
            this.inventory[par1] = par2ItemStack;
            this.onInventoryChanged();
        }
        else
        {
            ItemStack filteredStack = getLeader().filterStackToGroup(par2ItemStack);
            if (filteredStack != null)
            {
                this.inventory[par1] = par2ItemStack;
                this.onInventoryChanged();
            }
        }
        syncInventory();
    }

    public void syncInventory()
    {
        for (EntityPlayer player : crafters)
        {
            player.openContainer.detectAndSendChanges();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public abstract String getInvName();

    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
    public abstract boolean isInvNameLocalized();

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.length)
            {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        filter.readFromNBT(par1NBTTagCompound);
        blacklist = par1NBTTagCompound.getBoolean("blacklist");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);

        filter.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("blacklist", blacklist);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public abstract int getInventoryStackLimit();

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public abstract boolean isUseableByPlayer(EntityPlayer par1EntityPlayer);


    public abstract void openChest();

    public abstract void closeChest();

    /**
     * Should be called by the constructor of the Container of this tile, used to sync Inventory contents
     *
     * @param player The player opening the container
     */
    public void addCrafter(EntityPlayer player)
    {
        crafters.add(player);
    }

    /**
     * Should be called by onContainerClosed(...) of the Container of this this tile, used to stop syncing Inventory contents
     *
     * @param player The player closing the container
     */
    public void removeCrafter(EntityPlayer player)
    {
        crafters.remove(player);
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public abstract boolean isItemValidForSlot(int par1, ItemStack par2ItemStack);

    public Filter getFilter()
    {
        return filter;
    }

    public boolean getBlackList()
    {
        return blacklist;
    }

    public void setBlackList(boolean value)
    {
        blacklist = value;
    }
}
