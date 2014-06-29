package com.dynious.refinedrelocation.tileentity;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.transport.IPipeTile;
import cofh.api.energy.IEnergyHandler;
import cofh.api.transport.IItemConduit;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.mods.IC2Helper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.item.ItemFrozenLiquid;
import com.dynious.refinedrelocation.tileentity.energy.TileUniversalElectricity;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.dynious.refinedrelocation.helper.Tank;

public class TileHeater extends TileEntity implements IInventory, IFluidHandler
{
    private Tank tank = new Tank(1000, this);
    private ItemStack bufferStack = null;

    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileHeater();
    }

    @Override
    public void updateEntity()
    {
        if (!worldObj.isRemote)
        {
            if (bufferStack != null)
                addBuffer();
        }
    }

    // public void onBlocksChanged()
    // {
    //     tiles = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
    //     for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
    //     {
    //         TileEntity tile =  DirectionHelper.getTileAtSide(this, direction);
    //         tiles[direction.ordinal()] = tile;
    //     }
    // }

    public void addBuffer()
    {
        if (bufferStack != null)
        {
            tank.setFluid(ItemFrozenLiquid.getFluidStack(bufferStack));
            bufferStack = null;
        }
    }

    // public void outputFluids()
    // {
    //     FluidStack stack = tank.getFluid();
    //     for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
    //     {
    //         if (DirectionHelper.getTileAtSide(this, direction) instanceof IFluidHandler)
    //         {
    //             tank.setFluid(insertFluidStack(stack, direction.ordinal()));
    //         }
    //     }
    // }

    // public FluidStack insertFluidStack(FluidStack fluidStack, int side)
    // {
    //     TileEntity tile = tiles[side];
    //     if (tile != null)
    //     {
    //         if (tile instanceof IFluidHandler)
    //         {
    //             fluidStack.amount -= ((IFluidHandler)tile).fill(ForgeDirection.getOrientation(side).getOpposite(), fluidStack, true);
    //         }
    //     }

    //     if (fluidStack.amount == 0)
    //     {
    //         return null;
    //     }
    //     return fluidStack;
    // }

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
        return "heater";
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
        return (stack.getItem() instanceof ItemFrozenLiquid);
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
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxEmpty, boolean doDrain) {
        return tank.drain(maxEmpty, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return tank.drain(1000, doDrain);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection direction) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }
}
