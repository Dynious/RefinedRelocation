package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TravellingItem
{
    public static final byte timePerRelocator = 5;
    private ItemStack itemStack;
    private IRelocator startingPoint;
    private PathToRelocator path;
    private IRelocator currentRelocator;
    public byte counter;

    public TravellingItem(ItemStack itemStack, IRelocator startingPoint, PathToRelocator path)
    {
        this.itemStack = itemStack;
        this.startingPoint = startingPoint;
        currentRelocator = startingPoint;
        this.path = path;
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

    public ItemStack getItemStack()
    {
        return itemStack;
    }
}
