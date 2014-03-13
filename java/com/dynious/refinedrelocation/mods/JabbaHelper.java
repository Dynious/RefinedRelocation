package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.block.BlockSortingBarrel;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import mcp.mobius.betterbarrels.BetterBarrels;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class JabbaHelper
{
    public static void addBarrelBlock()
    {
        ModBlocks.sortingBarrel = new BlockSortingBarrel(BlockIds.SORTING_BARREL);
        GameRegistry.registerBlock(ModBlocks.sortingBarrel, Names.sortingBarrel);
    }

    public static void addBarrelRecipes()
    {
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.sortingBarrel), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(BetterBarrels.blockBarrel));
    }
}
