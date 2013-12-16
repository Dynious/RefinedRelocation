package com.dynious.blex.block;

import com.dynious.blex.item.ItemBlockExtender;
import com.dynious.blex.lib.BlockIds;
import com.dynious.blex.lib.Names;
import com.dynious.blex.lib.Settings;
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

        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 0), "igi", "geg", "ioi", 'i', Item.ingotIron, 'o', Block.obsidian, 'g', Block.thinGlass, 'e', Item.enderPearl);
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 1), "r r", " b ", "r r", 'r', Block.blockRedstone, 'b', new ItemStack(blockExtender, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 2), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(blockExtender, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 3), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(blockExtender, 1, 1));
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 3), "r r", " b ", "r r", 'r', Block.blockRedstone, 'b', new ItemStack(blockExtender, 1, 2));

        if (!Settings.DISABLE_WIRELESS_BLOCK_EXTENDER)
        {
            GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 4), "d d", " b ", "d d", 'd', Item.diamond, 'b', new ItemStack(blockExtender, 1, 3));
        }
    }
}
