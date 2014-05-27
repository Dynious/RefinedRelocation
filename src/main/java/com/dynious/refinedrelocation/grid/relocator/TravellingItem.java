package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.dynious.refinedrelocation.util.Vector3;
import com.google.common.primitives.Bytes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TravellingItem
{
    public static final byte timePerRelocator = 10;
    private ItemStack itemStack;
    private Vector3 startingPoint;
    private List<Byte> path;
    public byte input;
    public byte counter;

    public TravellingItem(ItemStack itemStack, Vector3 startingPoint, List<Byte> path, int inputSide)
    {
        this.itemStack = itemStack;
        this.startingPoint = startingPoint;
        this.path = path;
        this.input = (byte) inputSide;
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

    public Vector3 getStartingPoint()
    {
        return startingPoint;
    }

    public void setStartingPoint(Vector3 startingPoint)
    {
        this.startingPoint = startingPoint;
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
        return ((float) counter + partialRenderTime) / timePerRelocator;
    }

    public float getX(float clientSideProgress)
    {
        if (clientSideProgress <= 0.5F)
        {
            return 0.5F + (ForgeDirection.getOrientation(getInputSide()).offsetX * 0.5F) - (ForgeDirection.getOrientation(getInputSide()).offsetX * clientSideProgress);
        }
        else
        {
            return 0.5F + (ForgeDirection.getOrientation(getOutputSide()).offsetX * clientSideProgress) - (ForgeDirection.getOrientation(getOutputSide()).offsetX * 0.5F);
        }
    }

    public float getY(float clientSideProgress)
    {
        if (clientSideProgress <= 0.5F)
        {
            return 0.5F + (ForgeDirection.getOrientation(getInputSide()).offsetY * 0.5F) - (ForgeDirection.getOrientation(getInputSide()).offsetY * clientSideProgress);
        }
        else
        {
            return 0.5F + (ForgeDirection.getOrientation(getOutputSide()).offsetY * clientSideProgress) - (ForgeDirection.getOrientation(getOutputSide()).offsetY * 0.5F);
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
        startingPoint.writeToNBT(compound);
        compound.setByteArray("path", Bytes.toArray(path));
        compound.setByte("input", input);
        compound.setByte("counter", counter);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        itemStack = ItemStack.loadItemStackFromNBT(compound);
        startingPoint = Vector3.createFromNBT(compound);
        path = Bytes.asList(compound.getByteArray("path"));
        input = compound.getByte("input");
        counter = compound.getByte("counter");
    }

    public static TravellingItem createFromNBT(NBTTagCompound compound)
    {
        TravellingItem t = new TravellingItem(ItemStack.loadItemStackFromNBT(compound), Vector3.createFromNBT(compound), new ArrayList<Byte>(Bytes.asList(compound.getByteArray("path"))), compound.getByte("input"));
        t.counter = compound.getByte("counter");
        return t;
    }
}
