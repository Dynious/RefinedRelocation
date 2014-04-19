package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.lib.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TileSortingImporter extends TileSortingConnector implements IInventory
{
    private ItemStack[] bufferInventory = new ItemStack[1];

    public void onRightClick(EntityPlayer player)
    {
        if (player.getHeldItem() != null && bufferInventory[0] == null)
        {
            setInventorySlotContents(0, player.getHeldItem());
            player.inventory.mainInventory[player.inventory.currentItem] = null;
        }
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return bufferInventory[i];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.bufferInventory[par1] != null)
        {
            ItemStack itemstack;

            if (this.bufferInventory[par1].stackSize <= par2)
            {
                itemstack = this.bufferInventory[par1];
                this.bufferInventory[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.bufferInventory[par1].splitStack(par2);

                if (this.bufferInventory[par1].stackSize == 0)
                {
                    this.bufferInventory[par1] = null;
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

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.bufferInventory[par1] != null)
        {
            ItemStack itemstack = this.bufferInventory[par1];
            this.bufferInventory[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        //Convert!
        bufferInventory[0] = getSortingHandler().filterStackToGroup(itemstack, this, i);
    }

    @Override
    public String getInvName()
    {
        return Names.sortingImporter;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void openChest()
    {

    }

    @Override
    public void closeChest()
    {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return bufferInventory[0] == null;
    }
}
