package com.dynious.blex.item;

import com.dynious.blex.lib.ItemIds;
import com.dynious.blex.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItems
{
    public static ItemLinker linker;
    public static ItemFilteringUpgrade filteringUpgrade;

    public static void init()
    {
        linker = new ItemLinker(ItemIds.LINKER);
        filteringUpgrade = new ItemFilteringUpgrade(ItemIds.FILTERING_UPGRADE);

        GameRegistry.registerItem(linker, Names.linker);
        GameRegistry.registerItem(filteringUpgrade, Names.filteringUpgrade);

        GameRegistry.addShapedRecipe(new ItemStack(linker), "iri", "rer", "iri", 'i', Item.ingotIron, 'r', Item.redstone, 'e', Item.enderPearl);
        GameRegistry.addShapedRecipe(new ItemStack(filteringUpgrade), "g g", " p ", "g g", 'g', Item.ingotGold, 'p', Block.thinGlass);
    }
}
