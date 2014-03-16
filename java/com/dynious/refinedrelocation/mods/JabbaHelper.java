package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.block.BlockSortingBarrel;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import mcp.mobius.betterbarrels.BetterBarrels;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class JabbaHelper
{
    public static void addBarrelBlock()
    {
        ModBlocks.sortingBarrel = new BlockSortingBarrel();
        GameRegistry.registerBlock(ModBlocks.sortingBarrel, Names.sortingBarrel);
    }

    public static void addBarrelRecipes()
    {
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.sortingBarrel), "g g", " b ", "g g", 'g', Items.gold_ingot, 'b', new ItemStack(BetterBarrels.blockBarrel));
    }
}
