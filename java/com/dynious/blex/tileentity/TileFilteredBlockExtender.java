package com.dynious.blex.tileentity;

import com.dynious.blex.config.Filter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class TileFilteredBlockExtender extends TileBlockExtender
{
    private boolean blackList = false;
    private Filter filter = new Filter();

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack)
    {
        if (!super.isItemValidForSlot(i, itemStack))
        {
            return false;
        }
        return blackList? !doesItemStackPassFilter(itemStack): doesItemStackPassFilter(itemStack);
    }

    private boolean doesItemStackPassFilter(ItemStack itemStack)
    {
        String oreName = OreDictionary.getOreName(OreDictionary.getOreID(itemStack));
        if (filter.ingots && !oreName.contains("ingot"))
            return false;
        if (filter.ores && !oreName.contains("ore"))
            return false;
        if(filter.)
        return true;
    }
}
