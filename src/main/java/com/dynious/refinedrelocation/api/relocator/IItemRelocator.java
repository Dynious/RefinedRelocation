package com.dynious.refinedrelocation.api.relocator;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface IItemRelocator
{
    TileEntity getTileEntity();

    IRelocatorModule getRelocatorModule(int side);

    ItemStack insert(ItemStack itemStack, int side, boolean simulate);

    boolean getRedstoneState();

    TileEntity[] getConnectedInventories();

    IItemRelocator[] getConnectedRelocators();

    boolean connectsToSide(int side);

    boolean isStuffedOnSide(int side);
}
