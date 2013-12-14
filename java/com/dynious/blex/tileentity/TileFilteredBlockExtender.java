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
        if(filter.wood && !oreName.contains("wood"))
            return false;
        if (filter.planks && !oreName.contains("plank"))
            return false;
        if (filter.dusts && !(oreName.contains("dust") || oreName.contains("crushed")))
            return false;

        int index = itemStack.getItem().getCreativeTab().getTabIndex();
        for (int i = 0; i < filter.creativeTabs.length; i++)
        {
            if (filter.creativeTabs[i] && index != i)
            {
                return false;
            }
        }
        return true;
    }
}
