package com.dynious.refinedrelocation.creativetab;

import com.dynious.refinedrelocation.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabRefinedRelocation extends CreativeTabs
{

    public CreativeTabRefinedRelocation(int par1, String par2Str)
    {
        super(par1, par2Str);
    }

    @Override
    public Item getTabIconItem()
    {
        return null;
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(ModBlocks.blockExtender);
    }
}
