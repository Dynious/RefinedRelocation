package com.dynious.refinedrelocation.api.item;

import com.dynious.refinedrelocation.api.filter.IRelocatorModule;
import net.minecraft.item.ItemStack;

public interface IItemRelocatorModule
{
    public IRelocatorModule getRelocatorFilter(ItemStack stack);
}
