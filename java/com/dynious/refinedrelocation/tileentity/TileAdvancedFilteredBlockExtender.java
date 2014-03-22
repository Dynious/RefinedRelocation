package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.filter.FilterStandard;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.ArrayUtils;

public class TileAdvancedFilteredBlockExtender extends TileBlockExtender implements IAdvancedFilteredTile
{
    private boolean spreadItems = false;
    private byte[] insertDirection = {1, 1, 1, 1, 1, 1, 1};
    private int bestSlot;
    private boolean shouldUpdateBestSlot = true;
    private int lastSlotSide;
    private ItemStack lastStack;
    private FilterStandard filter = new FilterStandard();
    private byte maxStackSize = 64;
    public boolean restrictExtraction = false;

    @Override
    public boolean getRestrictExtraction()
    {
        return restrictExtraction;
    }

    @Override
    public void setRestrictionExtraction(boolean restrict)
    {
        restrictExtraction = restrict;
    }

    public byte[] getInsertDirections()
    {
        return insertDirection;
    }

    @Override
    public byte[] getInsertDirection()
    {
        return insertDirection;
    }

    public void setInsertDirection(int from, int value)
    {
        int numDirs = ForgeDirection.VALID_DIRECTIONS.length;
        value = (value % numDirs + numDirs) % numDirs;
        insertDirection[from] = (byte) value;
    }

    public void setConnectedSide(int connectedSide)
    {
        super.setConnectedSide(connectedSide);
        if (connectedDirection != ForgeDirection.UNKNOWN)
        {
            for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
            {
                insertDirection[i] = (byte) connectedDirection.getOpposite().ordinal();
            }
        }
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (spreadItems)
        {
            if (shouldUpdateBestSlot || lastSlotSide != i2 || !ItemStackHelper.areItemStacksEqual(itemStack, lastStack))
            {
                updateBestSlot(i2, itemStack);
                shouldUpdateBestSlot = false;
            }
            if (i != bestSlot || !super.canInsertItem(bestSlot, itemStack, i2))
            {
                return false;
            }
            shouldUpdateBestSlot = true;
            return filter.passesFilter(itemStack);
        }
        else
        {
            return super.canInsertItem(i, itemStack, i2) && filter.passesFilter(itemStack);
        }
    }

    private void updateBestSlot(int side, ItemStack itemStack)
    {
        int bestSize = Integer.MAX_VALUE;
        int[] invAccessibleSlots = getAccessibleSlotsFromSide(side);

        for (int j = 0; j < invAccessibleSlots.length; ++j)
        {
            int slot = invAccessibleSlots[j];

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
        lastSlotSide = side;
        lastStack = itemStack;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int i2)
    {
        return (super.canExtractItem(i, itemStack, i2) && !(restrictExtraction && filter.passesFilter(itemStack)));
    }

    @Override
    public byte getMaxStackSize()
    {
        return maxStackSize;
    }

    @Override
    public void setMaxStackSize(byte maxStackSize)
    {
        this.maxStackSize = maxStackSize;
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
    public int getInventoryStackLimit()
    {
        if (getInventory() != null)
        {
            return Math.min(super.getInventoryStackLimit(), maxStackSize);
        }
        return maxStackSize;
    }

    @Override
    public ForgeDirection getInputSide(ForgeDirection side)
    {
        return ForgeDirection.getOrientation(insertDirection[side.ordinal()]);
    }

    @Override
    public IFilterGUI getFilter()
    {
        return filter;
    }

    /*
    ComputerCraft interaction
    */

    /*

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public String getType()
    {
        return "advanced_filtered_block_extender";
    }

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public String[] getMethodNames()
    {
        return ArrayUtils.addAll(super.getMethodNames(), "getMaxStackSize", "setMaxStackSize", "getSpread", "setSpread", "getInputSide", "setInputSide", "isFilterEnabled", "setFilterEnabled");
    }

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        Object[] superArr = super.callMethod(computer, context, method, arguments);
        if (superArr != null)
        {
            return superArr;
        }
        switch (method)
        {
            case 2:
                return new Integer[]{(int) maxStackSize};
            case 3:
                if (arguments.length > 0 && arguments[0] instanceof Double)
                {
                    double arg = (Double) arguments[0];
                    if (arg >= 0 && arg <= Byte.MAX_VALUE)
                    {
                        setMaxStackSize((byte) arg);
                        return new Boolean[]{true};
                    }
                }
                return new Boolean[]{false};
            case 4:
                return new Boolean[]{spreadItems};
            case 5:
                if (arguments.length > 0 && arguments[0] instanceof Boolean)
                {
                    spreadItems = (Boolean) arguments[0];
                    return new Boolean[]{true};
                }
                return new Boolean[]{false};
            case 6:
                if (arguments.length > 0 && arguments[0] instanceof Double)
                {
                    double arg = (Double) arguments[0];
                    if (arg >= 0 && arg < ForgeDirection.VALID_DIRECTIONS.length)
                        return new Integer[]{(int) insertDirection[(byte) arg]};
                }
                return new Boolean[]{false};
            case 7:
                if (arguments.length > 1 && arguments[0] instanceof Double && arguments[1] instanceof Double)
                {
                    double side = (Double) arguments[0];
                    double value = (Double) arguments[1];
                    if (side >= 0 && side < ForgeDirection.VALID_DIRECTIONS.length && value >= 0 && value < ForgeDirection.VALID_DIRECTIONS.length)
                    {
                        insertDirection[(byte) side] = (byte) value;
                        return new Boolean[]{true};
                    }
                }
                return new Boolean[]{false};
            case 8:
                if (arguments.length > 0 && arguments[0] instanceof Double)
                {
                    double arg = (Double) arguments[0];
                    if (arg >= 0 && arg < filter.getSize())
                    {
                        return new Boolean[]{filter.getValue((int) arg)};
                    }
                    return null;
                }
            case 9:
                if (arguments.length > 1 && arguments[0] instanceof Double && arguments[1] instanceof Boolean)
                {
                    double arg = (Double) arguments[0];
                    if (arg >= 0 && arg < filter.getSize())
                    {
                        filter.setValue((int) arg, (Boolean) arguments[1]);
                        return new Boolean[]{true};
                    }
                }
                return new Boolean[]{false};
        }
        return null;
    }

    */

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        spreadItems = compound.getBoolean("spreadItems");
        insertDirection = compound.getByteArray("insertDirection");
        filter.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("spreadItems", spreadItems);
        compound.setByteArray("insertDirection", insertDirection);
        filter.writeToNBT(compound);
    }
}
