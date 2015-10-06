package com.dynious.refinedrelocation.api.item;

import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import net.minecraft.item.ItemStack;

public interface IItemRelocatorModule
{
    IRelocatorModule getRelocatorModule(ItemStack stack);
}
