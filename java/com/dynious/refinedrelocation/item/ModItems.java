package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Settings;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModItems
{
    public static ItemLinker linker;
    public static ItemSortingUpgrade sortingUpgrade;
    public static ItemPlayerRelocator playerRelocator;

    public static void init()
    {
        linker = new ItemLinker();
        sortingUpgrade = new ItemSortingUpgrade();
        playerRelocator = new ItemPlayerRelocator();

        GameRegistry.registerItem(linker, Names.linker);
        GameRegistry.registerItem(sortingUpgrade, Names.sortingUpgrade);
        GameRegistry.registerItem(playerRelocator, Names.playerRelocator);

        GameRegistry.addShapedRecipe(new ItemStack(linker), "iri", "rer", "iri", 'i', Items.iron_ingot, 'r', Items.redstone, 'e', Items.ender_pearl);
        GameRegistry.addShapedRecipe(new ItemStack(sortingUpgrade), "g g", " p ", "g g", 'g', Items.gold_ingot, 'p', Blocks.glass_pane);

        if (!Settings.DISABLE_PLAYER_RELOCATOR)
        {
        	GameRegistry.addShapedRecipe(new ItemStack(playerRelocator), "gbg", "ede", "gfg", 'g', Items.gold_ingot, 'b', Items.blaze_rod, 'e', Items.ender_pearl, 'd', Items.diamond, 'f', Items.fire_charge);
        }
    }
}
