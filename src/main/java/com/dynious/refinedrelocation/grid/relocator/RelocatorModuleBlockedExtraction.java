package com.dynious.refinedrelocation.grid.relocator;

import net.minecraft.item.ItemStack;

public class RelocatorModuleBlockedExtraction extends RelocatorModuleExtraction
{
    @Override
    public boolean passesFilter(ItemStack stack, boolean input)
    {
        return input;
    }
}
