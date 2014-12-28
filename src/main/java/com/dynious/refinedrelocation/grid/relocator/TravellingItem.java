package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.google.common.primitives.Bytes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TravellingItem
{
    public static final byte timePerRelocator = 10;
    public byte input;
    public byte counter;
    private ItemStack itemStack;
    private List<Byte> path;

    public TravellingItem(ItemStack itemStack, List<Byte> path, int inputSide)
    {
        this.itemStack = itemStack;
        this.path = path;
        this.input = (byte) inputSide;
    }

    /**
     * @param itemStack The transported ItemStack
     * @param path      The path of the item, the first entry must be the input side
     */
    public TravellingItem(ItemStack itemStack, List<Byte> path)
    {
        this.itemStack = itemStack;
        this.path = path;
        this.input = getPath().get(0);
        getPath().remove(0);
    }

    public static TravellingItem createFromNBT(NBTTagCompound compound)
    {
        ItemStack stack = ItemStack.loadItemStackFromNBT(compound);
        if (stack == null)
            return null;
        TravellingItem t = new TravellingItem(stack, new ArrayList<Byte>(Bytes.asList(compound.getByteArray("path"))), compound.getByte("input"));
        t.counter = compound.getByte("counter");
        return t;
    }

    public int getStackSize()
    {
        return itemStack.stackSize;
    }

    public boolean isItemSameAs(ItemStack itemStack)
    {
        return ItemStackHelper.areItemStacksEqual(this.itemStack, itemStack);
    }

    public List<Byte> getPath()
    {
        return path;
    }

    public byte onOutput()
    {
        counter = 0;

        byte side = getPath().get(0);
        getPath().remove(0);
        input = (byte) ForgeDirection.OPPOSITES[side];
        return side;
    }

    public ItemStack getItemStack()
    {
        return itemStack;
    }

    public byte getInputSide()
    {
        return input;
    }

    public byte getOutputSide()
    {
        return path.get(0);
    }

    public float getClientSideProgress(float partialRenderTime)
    {
        return ((float) counter + partialRenderTime) / (timePerRelocator - 1);
    }

    public float getX(float clientSideProgress)
    {
        if (clientSideProgress <= 0.5F)
        {
            return 0.6F + (ForgeDirection.getOrientation(getInputSide()).offsetX * 0.5F) - (ForgeDirection.getOrientation(getInputSide()).offsetX * clientSideProgress);
        }
        else
        {
            return 0.6F + (ForgeDirection.getOrientation(getOutputSide()).offsetX * clientSideProgress) - (ForgeDirection.getOrientation(getOutputSide()).offsetX * 0.5F);
        }
    }

    public float getY(float clientSideProgress)
    {
        if (clientSideProgress <= 0.5F)
        {
            return 0.6F + (ForgeDirection.getOrientation(getInputSide()).offsetY * 0.5F) - (ForgeDirection.getOrientation(getInputSide()).offsetY * clientSideProgress);
        }
        else
        {
            return 0.6F + (ForgeDirection.getOrientation(getOutputSide()).offsetY * clientSideProgress) - (ForgeDirection.getOrientation(getOutputSide()).offsetY * 0.5F);
        }
    }

    public float getZ(float clientSideProgress)
    {
        if (clientSideProgress <= 0.5F)
        {
            return 0.6F + (ForgeDirection.getOrientation(getInputSide()).offsetZ * 0.5F) - (ForgeDirection.getOrientation(getInputSide()).offsetZ * clientSideProgress);
        }
        else
        {
            return 0.6F + (ForgeDirection.getOrientation(getOutputSide()).offsetZ * clientSideProgress) - (ForgeDirection.getOrientation(getOutputSide()).offsetZ * 0.5F);
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        itemStack.writeToNBT(compound);
        compound.setByteArray("path", Bytes.toArray(path));
        compound.setByte("input", input);
        compound.setByte("counter", counter);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        itemStack = ItemStack.loadItemStackFromNBT(compound);
        path = Bytes.asList(compound.getByteArray("path"));
        input = compound.getByte("input");
        counter = compound.getByte("counter");
    }
}
