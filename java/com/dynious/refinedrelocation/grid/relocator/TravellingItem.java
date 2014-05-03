package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TravellingItem
{
    private ItemStack itemStack;
    private IRelocator startingPoint;
    private IRelocator endPoint;
    private List<Byte> decisions;

    public TravellingItem(ItemStack itemStack, IRelocator startingPoint, IRelocator endPoint, List<Byte> decisions)
    {
        this.itemStack = itemStack;
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
        this.decisions = decisions;
    }

    public int getStackSize()
    {
        return itemStack.stackSize;
    }

    public boolean isItemSameAs(ItemStack itemStack)
    {
        return ItemStackHelper.areItemStacksEqual(this.itemStack, itemStack);
    }
}
