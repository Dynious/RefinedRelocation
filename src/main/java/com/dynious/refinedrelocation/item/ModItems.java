package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.api.ModObjects;
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
    public static ItemRelocatorModule relocatorModule;
    public static ItemToolBox toolBox;

    public static void init()
    {
        linker = new ItemLinker();
        sortingUpgrade = new ItemSortingUpgrade();
        playerRelocator = new ItemPlayerRelocator();
        relocatorModule = new ItemRelocatorModule();
        toolBox = new ItemToolBox();

        ModObjects.linker = new ItemStack(linker);
        ModObjects.sortingUpgrade = new ItemStack(sortingUpgrade);
        ModObjects.playerRelocator = new ItemStack(playerRelocator);
        ModObjects.relocatorFilter = new ItemStack(relocatorModule);
        ModObjects.toolbox = new ItemStack(toolBox);

        GameRegistry.registerItem(linker, Names.linker);
        GameRegistry.registerItem(sortingUpgrade, Names.sortingUpgrade);
        GameRegistry.registerItem(playerRelocator, Names.playerRelocator);
        GameRegistry.registerItem(relocatorModule, Names.relocatorModule);
        GameRegistry.registerItem(toolBox, Names.toolbox);

        GameRegistry.addShapedRecipe(new ItemStack(linker), "iri", "rer", "iri", 'i', Items.iron_ingot, 'r', Items.redstone, 'e', Items.ender_pearl);
        GameRegistry.addShapedRecipe(new ItemStack(sortingUpgrade), "g g", " p ", "g g", 'g', Items.gold_ingot, 'p', Blocks.glass_pane);

        if (!Settings.DISABLE_PLAYER_RELOCATOR)
        {
        	GameRegistry.addShapedRecipe(new ItemStack(playerRelocator), "gbg", "ede", "gfg", 'g', Items.gold_ingot, 'b', Items.blaze_rod, 'e', Items.ender_pearl, 'd', Items.diamond, 'f', Items.fire_charge);
        }

        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 4, 0), "ibi", "b b", "ibi", 'i', Items.iron_ingot, 'b', Blocks.iron_bars);
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 1), "g g", " r ", "g g", 'g', Items.gold_ingot, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 2), "b b", " r ", "b b", 'b', Blocks.iron_bars, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 3), "b b", " r ", "b b", 'b', Blocks.redstone_block, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 4), "b b", " r ", "b b", 'b', Blocks.redstone_block, 'r', new ItemStack(relocatorModule, 1, 2));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 4), "b b", " r ", "b b", 'b', Blocks.iron_bars, 'r', new ItemStack(relocatorModule, 1, 3));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 5), "l l", " r ", "l l", 'l', new ItemStack(Items.dye, 1, 4), 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 6), "e e", " r ", "e e", 'e', Items.ender_pearl, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 7), "c d", " r ", "d c", 'c', Items.comparator, 'd', Items.redstone, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 8), "g g", " r ", "g g", 'g', Items.glowstone_dust, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addRecipe(new RecipeToolbox());
    }
}
