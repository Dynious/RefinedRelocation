package com.dynious.blex.tileentity;

import buildcraft.api.transport.IPipeTile;
import cofh.api.transport.IItemConduit;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileBuffer extends TileEntity implements ISidedInventory, IFluidHandler
{
    protected TileEntity[] tiles = new TileEntity[ForgeDirection.values().length];
    protected boolean firstRun = true;

    protected int bufferedSide = -1;
    protected ItemStack bufferedItemStack = null;

    public boolean containsItemStack = false;

    @Override
    public void updateEntity()
    {
        if (firstRun)
        {
            onBlocksChanged();
            firstRun = false;
            worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, 1, bufferedItemStack == null ? 0 : 1);
        }
        if (!worldObj.isRemote)
        {
            if (bufferedItemStack != null)
            {
                bufferedItemStack = outputItemStack(bufferedItemStack, bufferedSide);
                if (bufferedItemStack == null)
                {
                    worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, 1, 0);
                }
            }
        }
    }

    public void onBlocksChanged()
    {
        for (ForgeDirection direction : ForgeDirection.values())
        {
            tiles[direction.ordinal()] = worldObj.getBlockTileEntity(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
        }
    }

    public ItemStack setBufferedItemStack(ItemStack itemStack, int side)
    {
        if (bufferedItemStack == null)
        {
            bufferedItemStack = itemStack;
            bufferedSide = side;
            worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, 1, 1);
            return null;
        }
        else
        {
            return itemStack;
        }
    }

    public int getNextInsertSide(int currentInsertSide)
    {
        if (currentInsertSide + 1 < tiles.length)
        {
            return currentInsertSide + 1;
        }
        else
        {
            return -1;
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
        return bufferedItemStack == null;
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
        itemstack = outputItemStack(itemstack, slot);

        if (itemstack != null && setBufferedItemStack(itemstack, slot) != null)
        {
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, itemstack));
        }
    }

    public ItemStack outputItemStack(ItemStack itemstack, int inputSide)
    {
        int currentInsetSide = -1;
        while ((currentInsetSide = getNextInsertSide(currentInsetSide)) != -1)
        {
            if (currentInsetSide == inputSide)
                continue;
            itemstack = insertItemStack(itemstack, currentInsetSide);
            if (itemstack == null || itemstack.stackSize == 0)
            {
                return null;
            }
        }
        return itemstack;
    }

    public ItemStack insertItemStack(ItemStack itemstack, int side)
    {
        TileEntity tile = tiles[side];
        if (tile != null)
        {
            if (Loader.isModLoaded("CoFHCore") && tile instanceof IItemConduit)
            {
                return ((IItemConduit) tile).insertItem(ForgeDirection.getOrientation(side).getOpposite(), itemstack);
            }
            else if (Loader.isModLoaded("BuildCraft|Transport") && tile instanceof IPipeTile)
            {
                IPipeTile pipe = (IPipeTile) tile;
                if (pipe.isPipeConnected(ForgeDirection.getOrientation(side).getOpposite()))
                {
                    int size = pipe.injectItem(itemstack, true, ForgeDirection.getOrientation(side).getOpposite());
                    itemstack.stackSize -= size;
                    return itemstack;
                }
            }
            else if (tile instanceof IInventory)
            {
                return TileEntityHopper.insertStack((IInventory) tile, itemstack, ForgeDirection.OPPOSITES[side]);
            }
        }
        return itemstack;
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

    public boolean rotateBlock()
    {
        return false;
    }

    @Override
    public boolean receiveClientEvent(int eventId, int eventData)
    {
        switch (eventId)
        {
            case 1:
                containsItemStack = eventData == 1;
                return true;
        }
        return super.receiveClientEvent(eventId, eventData);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("bufferedSide"))
        {
            bufferedSide = par1NBTTagCompound.getByte("bufferedSide");
            this.bufferedItemStack = ItemStack.loadItemStackFromNBT(par1NBTTagCompound);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        if (bufferedItemStack != null)
        {
            par1NBTTagCompound.setByte("bufferedSide", (byte) bufferedSide);
            this.bufferedItemStack.writeToNBT(par1NBTTagCompound);
        }
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int inputAmount = resource.amount;
        int currentInsetSide = -1;
        while ((currentInsetSide = getNextInsertSide(currentInsetSide)) != -1)
        {
            if (currentInsetSide == from.ordinal())
                continue;
            resource = insertFluidStack(resource, currentInsetSide);
            if (resource == null || resource.amount == 0)
            {
                return inputAmount;
            }
        }

        return inputAmount - resource.amount;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[]{new FluidTankInfo(null, Integer.MAX_VALUE)};
    }

    public FluidStack insertFluidStack(FluidStack fluidStack, int side)
    {
        TileEntity tile = tiles[side];
        if (tile != null)
        {
            if (tile instanceof IFluidHandler)
            {
                fluidStack.amount -= ((IFluidHandler)tile).fill(ForgeDirection.getOrientation(side).getOpposite(), fluidStack, true);
            }
        }
        if (fluidStack.amount == 0)
        {
            return null;
        }
        return fluidStack;
    }
}
