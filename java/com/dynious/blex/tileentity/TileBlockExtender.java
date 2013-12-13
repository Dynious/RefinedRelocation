package com.dynious.blex.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileBlockExtender extends TileEntity implements ISidedInventory
{
    private ForgeDirection connectedDirection = ForgeDirection.UNKNOWN;
    private IInventory inventory;
    private int[] accessibleSlots;

    public void setConnectedSide(int connectedSide)
    {
        this.connectedDirection = ForgeDirection.getOrientation(connectedSide);
    }

    public void setInventory(IInventory inventory)
    {
        this.inventory = inventory;
        accessibleSlots = new int[inventory.getSizeInventory()];
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            accessibleSlots[i] = i;
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (connectedDirection != ForgeDirection.UNKNOWN)
        {
            if (inventory == null)
            {
                TileEntity tile = worldObj.getBlockTileEntity(this.xCoord + connectedDirection.offsetX, this.yCoord + connectedDirection.offsetY, this.zCoord + connectedDirection.offsetZ);
                if (tile != null)
                {
                    if (tile instanceof IInventory)
                    {
                        setInventory((IInventory)inventory);
                    }
                }
            }
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                return ((ISidedInventory)inventory).getAccessibleSlotsFromSide(i);
            }
            return accessibleSlots;
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                return ((ISidedInventory)inventory).canInsertItem(i, itemStack, i2);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int i2)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                return ((ISidedInventory)inventory).canExtractItem(i, itemStack, i2);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        if (inventory != null)
        {
            return inventory.getSizeInventory();
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        if (inventory != null)
        {
            return inventory.getStackInSlot(i);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int i2)
    {
        if (inventory != null)
        {
            return inventory.decrStackSize(i, i2);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (inventory != null)
        {
            return inventory.getStackInSlotOnClosing(i);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack)
    {
        if (inventory != null)
        {
            inventory.setInventorySlotContents(i, itemStack);
        }
    }

    @Override
    public String getInvName()
    {
        if (inventory != null)
        {
            return inventory.getInvName();
        }
        return null;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        if (inventory != null)
        {
            return inventory.isInvNameLocalized();
        }
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        if (inventory != null)
        {
            return inventory.getInventoryStackLimit();
        }
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        if (inventory != null)
        {
            return inventory.isUseableByPlayer(entityPlayer);
        }
        return false;
    }

    @Override
    public void openChest()
    {
        if (inventory != null)
        {
            inventory.openChest();
        }
    }

    @Override
    public void closeChest()
    {
        if (inventory != null)
        {
            inventory.closeChest();
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack)
    {
        if (inventory != null)
        {
            return inventory.isItemValidForSlot(i, itemStack);
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        connectedDirection = ForgeDirection.getOrientation(compound.getByte("side"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("side", (byte)connectedDirection.ordinal());
    }
}
