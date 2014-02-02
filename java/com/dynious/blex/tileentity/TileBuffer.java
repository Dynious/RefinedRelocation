package com.dynious.blex.tileentity;

import buildcraft.api.transport.IPipeTile;
import cofh.api.transport.IItemConduit;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileBuffer extends TileEntity implements ISidedInventory
{
    protected TileEntity[] tiles = new TileEntity[ForgeDirection.values().length];
    protected boolean firstRun = true;

    @Override
    public void updateEntity()
    {
        if (firstRun)
        {
            onBlocksChanged();
            firstRun = false;
        }
    }

    public void onBlocksChanged()
    {
        for (ForgeDirection direction : ForgeDirection.values())
        {
            tiles[direction.ordinal()] = worldObj.getBlockTileEntity(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return new int[]{var1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side)
    {
        return true;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack)
    {
        for (int i = 0; i < tiles.length; i++)
        {
            if (i == slot)
                continue;
            TileEntity tile = tiles[i];
            if (tile != null)
            {
                if (Loader.isModLoaded("CoFHCore") && tile instanceof IItemConduit)
                {
                    ItemStack returnedStack = ((IItemConduit) tile).insertItem(ForgeDirection.getOrientation(i).getOpposite(), itemstack);
                    if (returnedStack == null || returnedStack.stackSize == 0)
                        return;
                    else
                        itemstack = returnedStack;
                }
                else if (Loader.isModLoaded("BuildCraft|Transport") && tile instanceof IPipeTile)
                {
                    IPipeTile pipe = (IPipeTile) tile;
                    if (pipe.isPipeConnected(ForgeDirection.getOrientation(i).getOpposite()))
                    {
                        int size = pipe.injectItem(itemstack, true, ForgeDirection.getOrientation(i).getOpposite());
                        if (size >= itemstack.stackSize)
                            return;
                        else
                            itemstack.stackSize -= size;
                    }
                }
            }
        }
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, itemstack));
    }

    @Override
    public String getInvName()
    {
        return "buffer";
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
        return true;
    }
}
