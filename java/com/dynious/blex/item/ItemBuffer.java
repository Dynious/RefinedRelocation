package com.dynious.blex.item;

import com.dynious.blex.block.ModBlocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBuffer extends ItemBlock
{
    public ItemBuffer(int id)
    {
        super(id);
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
