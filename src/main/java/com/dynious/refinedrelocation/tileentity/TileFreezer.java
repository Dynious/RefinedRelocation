package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.item.ItemFrozenLiquid;
import net.minecraftforge.common.ForgeDirection;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.dynious.refinedrelocation.helper.Tank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileFreezer extends TileEntity implements IInventory, IFluidHandler
{
    private Tank tank = new Tank(1000, this);
    private ItemStack bufferStack = null;

    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileFreezer();
    }

    @Override
    public void updateEntity()
    {
        if (!worldObj.isRemote)
        {
            if (tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME)
            {
                FluidStack fluid = tank.getFluid();
                outputItem(fluid);
            }
        }
    }

    public void outputItem(FluidStack fluid)
    {
        if (bufferStack == null)
        {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                TileEntity tile = DirectionHelper.getTileAtSide(this, direction);
                if (tile instanceof IInventory)
                {
                    ItemStack stack = new ItemStack(ModItems.frozenLiquid, 1);
                    ItemFrozenLiquid.setFluid(stack, fluid);
                    tank.clearFluid();
                    bufferStack = stack;
                }
            }
        }
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
    public ItemStack decrStackSize(int slot, int itemAmount)
    {
        bufferStack.stackSize -= itemAmount;
        if (bufferStack.stackSize <= 0)
            bufferStack = null;

        return bufferStack;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public String getInvName()
    {
        return "freezer";
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return bufferStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        return bufferStack;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return false;
    }

    @Override
    public void onInventoryChanged()
    {
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        bufferStack = stack;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        tank.readFromNBT(compound);
        super.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        tank.writeToNBT(compound);
        super.writeToNBT(compound);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (resource == null)
        {
            return 0;
        }

        return tank.fill(resource, doFill);
    }

    public int fill(FluidStack fluidStack, boolean doFill)
    {
        return fluidStack.amount;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxEmpty, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection direction) {
        FluidTank compositeTank = new FluidTank(tank.getCapacity());

        int capacity = tank.getCapacity();

        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        Fluid tankFluid = tank.getFluidType();
        return tankFluid == fluid || tankFluid == null;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }
}
