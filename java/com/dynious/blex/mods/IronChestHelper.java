package com.dynious.blex.mods;

import com.dynious.blex.block.ModBlocks;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.IronChestType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class IronChestHelper
{
    public static void addIronChestRecipes()
    {
        for (int i = 0; i < IronChestType.values().length; i++)
        {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.filteringIronChest, 1, i), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(IronChest.ironChestBlock, 1, i));
        }
    }
}
