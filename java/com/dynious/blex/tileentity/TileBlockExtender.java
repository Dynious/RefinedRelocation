package com.dynious.blex.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileBlockExtender extends TileEntity implements ISidedInventory, IFluidHandler, IPowerReceptor, IEnergySink

{
    private ForgeDirection connectedDirection = ForgeDirection.UNKNOWN;
    private IInventory inventory;
    private int[] accessibleSlots;
    private IFluidHandler fluidHandler;
    private IPowerReceptor powerReceptor;
    private IEnergySink energySink;

    public void setConnectedSide(int connectedSide)
    {
        this.connectedDirection = ForgeDirection.getOrientation(connectedSide);
    }

    public void setInventory(IInventory inventory)
    {
        this.inventory = inventory;
        if (inventory != null)
        {
            accessibleSlots = new int[inventory.getSizeInventory()];
            for (int i = 0; i < inventory.getSizeInventory(); i++)
            {
                accessibleSlots[i] = i;
            }
        }
    }

    public void setFluidHandler(IFluidHandler fluidHandler)
    {
        this.fluidHandler = fluidHandler;
    }

    public void setPowerReceptor(IPowerReceptor powerReceptor)
    {
        this.powerReceptor = powerReceptor;
    }

    public void setEnergySink(IEnergySink energySink)
    {
        this.energySink = energySink;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (connectedDirection != ForgeDirection.UNKNOWN)
        {
            if (inventory == null && fluidHandler == null)
            {
                TileEntity tile = worldObj.getBlockTileEntity(this.xCoord + connectedDirection.offsetX, this.yCoord + connectedDirection.offsetY, this.zCoord + connectedDirection.offsetZ);
                if (tile != null)
                {
                    if (tile instanceof IInventory)
                    {
                        setInventory((IInventory)tile);
                    }
                    if (tile instanceof IFluidHandler)
                    {
                        setFluidHandler((IFluidHandler)tile);
                    }
                    if (tile instanceof IPowerReceptor)
                    {
                        setPowerReceptor((IPowerReceptor)tile);
                    }
                    if (tile instanceof IEnergySink)
                    {
                        setEnergySink((IEnergySink) tile);
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

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.fill(from, resource, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.drain(from, resource, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.drain(from, maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.canFill(from, fluid);
        }
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.canDrain(from, fluid);
        }
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.getTankInfo(from);
        }
        return new FluidTankInfo[0];
    }

    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection forgeDirection)
    {
        if (powerReceptor != null)
        {
            return powerReceptor.getPowerReceiver(forgeDirection);
        }
        return null;
    }

    @Override
    public void doWork(PowerHandler powerHandler)
    {
        if (powerReceptor != null)
        {
            powerReceptor.doWork(powerHandler);
        }
    }

    @Override
    public World getWorld()
    {
        if (powerReceptor != null)
        {
            return powerReceptor.getWorld();
        }
        return null;
    }

    @Override
    public double demandedEnergyUnits()
    {
        if (energySink != null)
        {
            return energySink.demandedEnergyUnits();
        }
        return 0;
    }

    @Override
    public double injectEnergyUnits(ForgeDirection forgeDirection, double v)
    {
        if (energySink != null)
        {
            return energySink.injectEnergyUnits(forgeDirection, v);
        }
        return 0;
    }

    @Override
    public int getMaxSafeInput()
    {
        if (energySink != null)
        {
            return energySink.getMaxSafeInput();
        }
        return 0;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection)
    {
        if (energySink != null)
        {
            return energySink.acceptsEnergyFrom(tileEntity, forgeDirection);
        }
        return false;
    }
}
