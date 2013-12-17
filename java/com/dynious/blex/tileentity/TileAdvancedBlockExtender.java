package com.dynious.blex.tileentity;

import buildcraft.api.power.PowerHandler;
import com.dynious.blex.helper.ItemStackHelper;
import cpw.mods.fml.common.Optional;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class TileAdvancedBlockExtender extends TileBlockExtender
{
    public boolean spreadItems = false;
    private byte[] insertDirection;
    private int bestSlot;
    private boolean shouldUpdateBestSlot = true;
    private int lastSide;
    private ItemStack lastStack;
    private byte maxStackSize = 64;

    public TileAdvancedBlockExtender()
    {
        insertDirection = new byte[ForgeDirection.values().length];
        for (byte i = 0; i < insertDirection.length; i++)
        {
            insertDirection[i] = i;
        }
    }

    public byte[] getInsertDirection()
    {
        return insertDirection;
    }

    public void setInsertDirection(int from, int value)
    {
        if (value > 5)
            value = 0;
        insertDirection[from] = (byte) value;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (spreadItems)
        {
            if (lastSide != insertDirection[i2] || !ItemStackHelper.areItemStacksEqual(itemStack, lastStack) || shouldUpdateBestSlot)
            {
                updateBestSlot(insertDirection[i2], itemStack);
                shouldUpdateBestSlot = false;
            }
            if (i != bestSlot || !super.canInsertItem(bestSlot, itemStack, insertDirection[i2]))
            {
                return false;
            }
            shouldUpdateBestSlot = true;
            return true;
        }
        else
        {
            return super.canInsertItem(i, itemStack, insertDirection[i]);
        }
    }

    private void updateBestSlot(int side, ItemStack itemStack)
    {
        int bestSize = Integer.MAX_VALUE;
        for (int slot = 0; slot < getSizeInventory(); slot++)
        {
            ItemStack stack = getStackInSlot(slot);
            if (!super.canInsertItem(slot, itemStack, side))
            {
                continue;
            }
            if (stack == null)
            {
                bestSlot = slot;
                break;
            }
            if (ItemStackHelper.areItemStacksEqual(itemStack, stack) && stack.stackSize < bestSize)
            {
                bestSlot = slot;
                bestSize = stack.stackSize;
            }
        }
        lastSide = side;
        lastStack = itemStack;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                return ((ISidedInventory) inventory).getAccessibleSlotsFromSide(insertDirection[i]);
            }
            return accessibleSlots;
        }
        return new int[0];
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int i2)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                if (((ISidedInventory) inventory).canExtractItem(i, itemStack, insertDirection[i2]))
                {
                    objectTransported();
                    return true;
                }
                return false;
            }
            objectTransported();
            return true;
        }
        return false;
    }

    public void setMaxStackSize(byte maxStackSize)
    {
        this.maxStackSize = maxStackSize;
    }

    @Override
    public int getInventoryStackLimit()
    {
        if (inventory != null)
        {
            return Math.min(super.getInventoryStackLimit(), maxStackSize);
        }
        return maxStackSize;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (fluidHandler != null)
        {
            int amount = fluidHandler.fill(ForgeDirection.getOrientation(insertDirection[from.ordinal()]), resource, doFill);
            if (amount > 0 && doFill)
            {
                objectTransported();
            }
            return amount;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (fluidHandler != null)
        {
            FluidStack amount = fluidHandler.drain(ForgeDirection.getOrientation(insertDirection[from.ordinal()]), resource, doDrain);
            if (amount.amount > 0 && doDrain)
            {
                objectTransported();
            }
            return amount;
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (fluidHandler != null)
        {
            FluidStack amount = fluidHandler.drain(ForgeDirection.getOrientation(insertDirection[from.ordinal()]), maxDrain, doDrain);
            if (amount.amount > 0 && doDrain)
            {
                objectTransported();
            }
            return amount;
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.canFill(ForgeDirection.getOrientation(insertDirection[from.ordinal()]), fluid);
        }
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.canDrain(ForgeDirection.getOrientation(insertDirection[from.ordinal()]), fluid);
        }
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.getTankInfo(ForgeDirection.getOrientation(insertDirection[from.ordinal()]));
        }
        return new FluidTankInfo[0];
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection forgeDirection)
    {
        if (powerReceptor != null)
        {
            return powerReceptor.getPowerReceiver(ForgeDirection.getOrientation(insertDirection[forgeDirection.ordinal()]));
        }
        return null;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double injectEnergyUnits(ForgeDirection forgeDirection, double v)
    {
        if (energySink != null)
        {
            double amount = energySink.injectEnergyUnits(ForgeDirection.getOrientation(insertDirection[forgeDirection.ordinal()]), v);
            if (amount > 0)
            {
                objectTransported();
            }
            return amount;
        }
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection)
    {
        if (energySink != null)
        {
            return energySink.acceptsEnergyFrom(tileEntity, ForgeDirection.getOrientation(insertDirection[forgeDirection.ordinal()]));
        }
        return false;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        if (energyHandler != null)
        {
            int amount = energyHandler.receiveEnergy(ForgeDirection.getOrientation(insertDirection[forgeDirection.ordinal()]), i, b);
            if (amount > 0 && b)
            {
                objectTransported();
            }
            return amount;
        }
        return 0;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        if (energyHandler != null)
        {
            int amount = energyHandler.extractEnergy(ForgeDirection.getOrientation(insertDirection[forgeDirection.ordinal()]), i, b);
            if (amount > 0 && b)
            {
                objectTransported();
            }
            return amount;
        }
        return 0;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public boolean canInterface(ForgeDirection forgeDirection)
    {
        if (energyHandler != null)
        {
            return energyHandler.canInterface(ForgeDirection.getOrientation(insertDirection[forgeDirection.ordinal()]));
        }
        return false;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getEnergyStored(ForgeDirection forgeDirection)
    {
        if (energyHandler != null)
        {
            return energyHandler.getEnergyStored(ForgeDirection.getOrientation(insertDirection[forgeDirection.ordinal()]));
        }
        return 0;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection)
    {
        if (energyHandler != null)
        {
            return energyHandler.getMaxEnergyStored(ForgeDirection.getOrientation(insertDirection[forgeDirection.ordinal()]));
        }
        return 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        spreadItems = compound.getBoolean("spreadItems");
        insertDirection = compound.getByteArray("insertDirection");
        maxStackSize = compound.getByte("maxStackSize");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("spreadItems", spreadItems);
        compound.setByteArray("insertDirection", insertDirection);
        compound.setByte("maxStackSize", maxStackSize);
    }
}
