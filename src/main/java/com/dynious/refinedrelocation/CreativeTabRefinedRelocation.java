package com.dynious.refinedrelocation;

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
        return new ItemStack(ModBlocks.blockExtender).getItem();
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(ModBlocks.blockExtender);
    }
}
