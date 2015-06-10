package com.dynious.refinedrelocation.api.filter;

import net.minecraft.item.ItemStack;

public interface IMultiFilter extends IFilterGUI
{

    boolean passesFilter(ItemStack itemStack, FilterResult result);

}
