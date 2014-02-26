package com.dynious.blex.tileentity;

import com.dynious.blex.helper.ItemStackHelper;
import cpw.mods.fml.common.Optional;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import org.apache.commons.lang3.ArrayUtils;

public class TileAdvancedBlockExtender extends TileBlockExtender implements IAdvancedTile
{
    private boolean spreadItems = false;
    private byte[] insertDirection = {1, 1, 1, 1, 1, 1, 1};
    private int bestSlot;
    private boolean shouldUpdateBestSlot = true;
    private int lastSide;
    private ItemStack lastStack;
    private byte maxStackSize = 64;

    @Override
    public byte[] getInsertDirection()
    {
        return insertDirection;
    }

    @Override
    public void setInsertDirection(int from, int value)
    {
        int numDirs = ForgeDirection.VALID_DIRECTIONS.length;
        value = (value % numDirs + numDirs) % numDirs;
        insertDirection[from] = (byte) value;
    }

    @Override
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

    public boolean getSpreadItems()
    {
        return spreadItems;
    }

    public void setSpreadItems(boolean spreadItems)
    {
        this.spreadItems = spreadItems;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (spreadItems)
        {
            if (shouldUpdateBestSlot || lastSide != i2 || !ItemStackHelper.areItemStacksEqual(itemStack, lastStack))
            {
                updateBestSlot(i2, itemStack);
                shouldUpdateBestSlot = false;
            }
            if (i != bestSlot || !super.canInsertItem(bestSlot, itemStack, i2))
            {
                return false;
            }
            shouldUpdateBestSlot = true;
            return true;
        }
        else
        {
            return super.canInsertItem(i, itemStack, i2);
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
                return ((ISidedInventory) inventory).getAccessibleSlotsFromSide(getInputSide(ForgeDirection.getOrientation(i)).ordinal());
            }
            return accessibleSlots;
        }
        return new int[0];
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
    public int getInventoryStackLimit()
    {
        if (inventory != null)
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

    /*
    ComputerCraft interaction
    */

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public String getType()
    {
        return "advanced_block_extender";
    }

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public String[] getMethodNames()
    {
        return ArrayUtils.addAll(super.getMethodNames(), "getMaxStackSize", "setMaxStackSize", "getSpread", "setSpread", "getInputSide", "setInputSide");
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
        }
        return null;
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
