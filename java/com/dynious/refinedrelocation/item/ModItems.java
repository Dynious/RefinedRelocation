package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.lib.ItemIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Settings;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItems
{

    private static final int ID_SHIFT = 256;

    public static void init()
    {
        ItemObj.linker = new ItemLinker(ItemIds.LINKER - ID_SHIFT);
        ItemObj.sortingUpgrade = new ItemSortingUpgrade(ItemIds.SORTING_UPGRADE - ID_SHIFT);
        ItemObj.playerRelocator = new ItemPlayerRelocator(ItemIds.PLAYER_RELOCATOR - ID_SHIFT);

        GameRegistry.registerItem(ItemObj.linker, Names.linker);
        GameRegistry.registerItem(ItemObj.sortingUpgrade, Names.sortingUpgrade);
        GameRegistry.registerItem(ItemObj.playerRelocator, Names.playerRelocator);

        GameRegistry.addShapedRecipe(new ItemStack(ItemObj.linker), "iri", "rer", "iri", 'i', Item.ingotIron, 'r', Item.redstone, 'e', Item.enderPearl);
        GameRegistry.addShapedRecipe(new ItemStack(ItemObj.sortingUpgrade), "g g", " p ", "g g", 'g', Item.ingotGold, 'p', Block.thinGlass);

        if (!Settings.DISABLE_PLAYER_RELOCATOR)
        {
            GameRegistry.addShapedRecipe(new ItemStack(ItemObj.playerRelocator), "gbg", "ede", "gfg", 'g', Item.ingotGold, 'b', Item.blazeRod, 'e', Item.enderPearl, 'd', Item.diamond, 'f', Item.fireballCharge);
        }
    }
}
