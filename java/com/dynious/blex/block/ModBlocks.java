package com.dynious.blex.block;

import com.dynious.blex.item.ItemBlockExtender;
import com.dynious.blex.lib.BlockIds;
import com.dynious.blex.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModBlocks
{
    public static BlockExtender blockExtender;

    public static void init()
    {
        blockExtender = new BlockExtender(BlockIds.BLOCK_EXTENDER);

        GameRegistry.registerBlock(blockExtender, ItemBlockExtender.class, Names.blockExtender);

        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 0), "ioi", "geg", "igi", 'i', Item.ingotIron, 'o', Block.obsidian, 'g', Block.thinGlass, 'e', Item.enderPearl);
    }
}
