package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

public class TravellingItem
{
    public static final byte timePerRelocator = 5;
    private ItemStack itemStack;
    private IRelocator startingPoint;
    private PathToRelocator path;
    public byte input;
    public byte counter;

    public TravellingItem(ItemStack itemStack, IRelocator startingPoint, PathToRelocator path, int inputSide)
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

    public PathToRelocator getPath()
    {
        return path;
    }

    public byte onOutput()
    {
        counter = 0;
        
        byte side = getPath().PATH.get(0);
        getPath().PATH.remove(0);
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
        return path.PATH.get(0);
    }
}
