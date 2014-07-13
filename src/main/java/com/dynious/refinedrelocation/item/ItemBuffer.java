package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBuffer extends ItemBlock
{
    public ItemBuffer(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    public String getUnlocalizedName(ItemStack i)
    {
        return ModBlocks.buffer.getUnlocalizedName() + i.getItemDamage();
    }
}
