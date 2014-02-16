package com.dynious.blex.item;

import com.dynious.blex.block.ModBlocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemFilteringChest extends ItemBlock
{
    public ItemFilteringChest(int par1)
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
        return ModBlocks.filteringChest.getUnlocalizedName() + i.getItemDamage();
    }
}
