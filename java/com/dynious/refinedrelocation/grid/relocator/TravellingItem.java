package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

public class TravellingItem
{
    public static final byte timePerRelocator = 10;
    private ItemStack itemStack;
    private IRelocator startingPoint;
    private List<Byte> path;
    public byte input;
    public byte counter;

    public TravellingItem(ItemStack itemStack, IRelocator startingPoint, List<Byte> path, int inputSide)
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

    public IRelocator getStartingPoint()
    {
        return startingPoint;
    }

    public void setStartingPoint(IRelocator startingPoint)
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

    /*
    Client Side methods
     */

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
            return 0.5F + (ForgeDirection.getOrientation(getInputSide()).offsetZ * 0.5F) - (ForgeDirection.getOrientation(getInputSide()).offsetZ * clientSideProgress);
        }
        else
        {
            return 0.5F + (ForgeDirection.getOrientation(getOutputSide()).offsetZ * clientSideProgress) - (ForgeDirection.getOrientation(getOutputSide()).offsetZ * 0.5F);
        }
    }
}
