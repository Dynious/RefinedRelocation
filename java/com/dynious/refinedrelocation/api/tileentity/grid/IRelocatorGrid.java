package com.dynious.refinedrelocation.api.tileentity.grid;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import net.minecraft.item.ItemStack;

public interface IRelocatorGrid extends IGrid
{
    public TravellingItem findOutput(ItemStack itemStack, IRelocator relocator, int side);

    public void travelItem(TravellingItem item, int side);
}
