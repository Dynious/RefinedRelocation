package com.dynious.refinedrelocation.api.filter;

import net.minecraft.item.ItemStack;

public interface IFilter {

    boolean passesFilter(ItemStack itemStack);

}
