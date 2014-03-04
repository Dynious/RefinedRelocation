package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.block.ModBlocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemFilteringIronChest extends ItemBlock
{
    public ItemFilteringIronChest(int par1)
    {
        super(par1);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    public String getUnlocalizedName(ItemStack i)
    {
        return ModBlocks.filteringIronChest.getUnlocalizedName() + i.getItemDamage();
    }
}
