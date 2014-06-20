package com.dynious.refinedrelocation.tileentity;

import buildcraft.api.power.PowerHandler;
import com.dynious.refinedrelocation.lib.Mods;
import com.google.common.primitives.Bytes;
import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TileAdvancedBuffer extends TileBuffer implements IAdvancedTile
{
    private byte[] outputPrioritiesArrayProxy = {0, 1, 2, 3, 4, 5};
    private List<Byte> outputPriorities = new ArrayList<Byte>(Bytes.asList(outputPrioritiesArrayProxy));
    private boolean spreadItems = false;
    private ForgeDirection lastItemOutputSide = ForgeDirection.UNKNOWN;
    private ForgeDirection lastFluidOutputSide = ForgeDirection.UNKNOWN;
    private ForgeDirection lastBCEnergyOutputSide = ForgeDirection.UNKNOWN;
    private ForgeDirection lastIC2EnergyOutputSide = ForgeDirection.UNKNOWN;

    /*
    private ForgeDirection lastCoFHEnergyOutputSide = ForgeDirection.UNKNOWN;
    private ForgeDirection lastUEEnergyOutputSide = ForgeDirection.UNKNOWN;
    */

    public static final byte NULL_PRIORITY = (byte) (ForgeDirection.VALID_DIRECTIONS.length);

    public byte[] getInsertDirection()
    {
        return outputPrioritiesArrayProxy;
    }

    public void setInsertDirection(int from, int value)
    {
        setPriorityOfSideTo(from, value);
    }

    public void setPriorityOfSideTo(int side, int priority)
    {
        priority = Math.min(NULL_PRIORITY, Math.max(0, priority));
        if (getPriority(side) < priority && priority == outputPriorities.size())
            priority = NULL_PRIORITY;

        outputPriorities.remove(new Byte((byte) side));
        if (priority != NULL_PRIORITY)
            outputPriorities.add(Math.min(outputPriorities.size(), priority), (byte) side);

        for (int i = 0; i < outputPrioritiesArrayProxy.length; i++)
        {
            outputPrioritiesArrayProxy[i] = getPriority(i);
        }
    }

    public byte getPriority(int side)
    {
        int priority = outputPriorities.indexOf((byte) side);
        if (priority == -1)
            priority = NULL_PRIORITY;
        return (byte) priority;
    }

    @Override
    public byte getMaxStackSize()
    {
        return 0;
    }

    @Override
    public void setMaxStackSize(byte maxStackSize)
    {

    }

    @Override
    public boolean getSpreadItems()
    {
        return spreadItems;
    }

    @Override
    public void setSpreadItems(boolean spreadItems)
    {
        this.spreadItems = spreadItems;
    }

    @Override
    public List<ForgeDirection> getOutputSidesForInsertDirection(ForgeDirection insertDirection) 
    {
        List<ForgeDirection> outputSides = new ArrayList<ForgeDirection>();
        for (Byte prioritySide : outputPriorities)
        {
            ForgeDirection side = ForgeDirection.getOrientation(prioritySide);

            if (side != insertDirection)
                outputSides.add(side);
        }
        return outputSides;
    }

    public List<ForgeDirection> getOutputSidesForInsertDirection(ForgeDirection insertDirection, ForgeDirection lastOutputSide)
    {
        List<ForgeDirection> outputSides = new ArrayList<ForgeDirection>();
        if (outputPriorities.size() > 0)
        {
            // indexOf will return -1 for any value not found, which will then get bumped up to 0
            int startPriorityIndex = (outputPriorities.indexOf((byte) lastOutputSide.ordinal()) + 1) % outputPriorities.size();
            for (int i=0, priorityIndexOfSide=startPriorityIndex; i<outputPriorities.size(); i++, priorityIndexOfSide = (priorityIndexOfSide + 1) % outputPriorities.size())
            {
                ForgeDirection side = ForgeDirection.getOrientation(outputPriorities.get(priorityIndexOfSide));
                if (side != insertDirection)
                    outputSides.add(side);
            }
        }
        return outputSides;
    }

    @Override
    public ItemStack outputItemStack(ItemStack itemstack, int inputSide)
    {
        if (spreadItems)
        {
            boolean didInsert;
            List<ForgeDirection> outputSides = getOutputSidesForInsertDirection(ForgeDirection.getOrientation(inputSide), lastItemOutputSide);
            ItemStack tempStack = itemstack.copy();
            tempStack.stackSize = 1;

            do
            {
                didInsert = false;
                for (ForgeDirection outputSide : outputSides)
                {
                    lastItemOutputSide = outputSide;
                    ItemStack returnedStack = insertItemStack(tempStack.copy(), outputSide.ordinal());
                    if (returnedStack == null || returnedStack.stackSize == 0)
                    {
                        itemstack.stackSize--;
                        didInsert = true;
                        
                        if (itemstack == null || itemstack.stackSize == 0)
                            return null;
                    }
                }
            } while (didInsert);
        }
        else
        {
            return super.outputItemStack(itemstack, inputSide);
        }
        return itemstack;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int inputAmount = resource.amount;
        if (spreadItems)
        {
            boolean didFill;
            List<ForgeDirection> outputSides = getOutputSidesForInsertDirection(from, lastFluidOutputSide);
            FluidStack tempStack = resource.copy();
            tempStack.amount = outputSides.isEmpty() ? 0 : (int) Math.ceil(resource.amount / outputSides.size());

            do
            {
                didFill = false;
                for (ForgeDirection outputSide : outputSides)
                {
                    lastFluidOutputSide = outputSide;
                    FluidStack returnedStack = insertFluidStack(tempStack.copy(), outputSide.ordinal());
                    if (returnedStack == null || returnedStack.amount == 0)
                    {
                        int amountFilled = returnedStack == null ? tempStack.amount : tempStack.amount - returnedStack.amount;
                        resource.amount -= Math.min(resource.amount, amountFilled);
                        didFill = true;

                        if (resource == null || resource.amount == 0)
                            return inputAmount;
                    }
                }
            } while (didFill);
        }
        else
        {
            return super.fill(from, resource, doFill);
        }
        return inputAmount - resource.amount;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
    {
        double inputAmount = amount;
        if (spreadItems)
        {
            boolean didInject;
            List<ForgeDirection> outputSides = getOutputSidesForInsertDirection(directionFrom, lastIC2EnergyOutputSide);
            double amountPerStack = outputSides.isEmpty() ? 0 : amount / outputSides.size();

            do
            {
                didInject = false;
                for (ForgeDirection outputSide : outputSides)
                {
                    lastIC2EnergyOutputSide = outputSide;
                    double amountLeft = insertEnergyUnits(amountPerStack, outputSide.ordinal());
                    if (amountLeft != amountPerStack)
                    {
                        amount -= Math.min(amount, amountPerStack - amountLeft);
                        didInject = true;

                        if (amount == 0)
                            return inputAmount;
                    }
                }
            } while (didInject);
        }
        else
        {
            return super.injectEnergyUnits(directionFrom, amount);
        }
        return inputAmount - amount;
    }

    @Optional.Method(modid = Mods.BC_API_POWER_ID)
    @Override
    public void doWork(PowerHandler powerHandler)
    {
        if (spreadItems)
        {
            boolean didUseEnergy;
            List<ForgeDirection> outputSides = getOutputSidesForInsertDirection(ForgeDirection.UNKNOWN, lastBCEnergyOutputSide);
            double amountPerStack = outputSides.isEmpty() ? 0 : powerHandler.getEnergyStored() / outputSides.size();

            do
            {
                didUseEnergy = false;
                for (ForgeDirection outputSide : outputSides)
                {
                    lastBCEnergyOutputSide = outputSide;
                    double usedEnergy = powerHandler.getEnergyStored() - insertMinecraftJoules(amountPerStack, outputSide.ordinal());
                    if (usedEnergy > 0)
                    {
                        powerHandler.useEnergy(usedEnergy, usedEnergy, true);
                        didUseEnergy = true;

                        if (powerHandler.getEnergyStored() == 0)
                            return;
                    }
                }
            } while (didUseEnergy);
        }
        else
        {
            super.doWork(powerHandler);
        }
    }

    /*

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        int inputAmount = i;
        if (spreadItems)
        {
            boolean didReceive;
            List<ForgeDirection> outputSides = getOutputSidesForInsertDirection(forgeDirection, lastCoFHEnergyOutputSide);
            int amountPerStack = outputSides.isEmpty() ? 0 : (int) Math.ceil(i / outputSides.size());

            do
            {
                didReceive = false;
                for (ForgeDirection outputSide : outputSides)
                {
                    lastCoFHEnergyOutputSide = outputSide;
                    int amountLeft = insertRedstoneFlux(amountPerStack, outputSide.ordinal(), b);
                    if (amountLeft != amountPerStack)
                    {
                        i -= Math.min(i, amountPerStack - amountLeft);
                        didReceive = true;

                        if (i == 0)
                            return inputAmount;
                    }
                }
            } while (didReceive);
        }
        else
        {
            return super.receiveEnergy(forgeDirection, i, b);
        }
        return inputAmount - i;
    }

    @Optional.Method(modid = "UniversalElectricity")
    @Override
    public long onReceiveEnergy(ForgeDirection direction, long l, boolean b)
    {
        long inputAmount = l;
        if (spreadItems)
        {
            boolean didReceive;
            List<ForgeDirection> outputSides = getOutputSidesForInsertDirection(direction, lastUEEnergyOutputSide);
            long amountPerStack = outputSides.isEmpty() ? 0 : (long) Math.ceil(l / outputSides.size());

            do
            {
                didReceive = false;
                for (ForgeDirection outputSide : outputSides)
                {
                    lastUEEnergyOutputSide = outputSide;
                    long amountLeft = insertUEEnergy(amountPerStack, outputSide.ordinal(), b);
                    if (amountLeft != amountPerStack)
                    {
                        l -= Math.min(l, amountPerStack - amountLeft);
                        didReceive = true;

                        if (l == 0)
                            return inputAmount;
                    }
                }
            } while (didReceive);
        }
        else
        {
            return super.onReceiveEnergy(direction, l, b);
        }
        return inputAmount - l;
    }

    */

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        outputPriorities.clear();
        byte byteArrayPriorities[] = compound.getByteArray("insertPriority");
        for (int priority = 0; priority < byteArrayPriorities.length; priority++)
        {
            setPriorityOfSideTo(byteArrayPriorities[priority], priority);
        }
        // any side not included in the saved data has a null priority
        for (byte i = 0; i < outputPrioritiesArrayProxy.length; i++)
        {
            if (!outputPriorities.contains(i))
                outputPrioritiesArrayProxy[i] = NULL_PRIORITY;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        byte byteArrayPriorities[] = new byte[outputPriorities.size()];
        for (int priority = 0; priority < outputPriorities.size(); priority++)
        {
            byteArrayPriorities[priority] = outputPriorities.get(priority);
        }
        compound.setByteArray("insertPriority", byteArrayPriorities);
    }
}
