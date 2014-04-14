package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.block.ModBlocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSortingConnector extends ItemBlock
{
    public ItemSortingConnector(int id)
    {
        super(id);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return ModBlocks.sortingConnector.getUnlocalizedName() + stack.getItemDamage();
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }
}
