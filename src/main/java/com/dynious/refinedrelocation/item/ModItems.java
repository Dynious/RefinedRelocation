package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.lib.ItemIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Settings;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItems
{
    public static ItemLinker linker;
    public static ItemSortingUpgrade sortingUpgrade;
    public static ItemPlayerRelocator playerRelocator;
    public static ItemRelocatorModule relocatorModule;
    public static ItemToolBox toolBox;
    public static ItemFrozenLiquid frozenLiquid;

    public static final int ID_SHIFT = 256;

    public static void init()
    {
        linker = new ItemLinker(ItemIds.LINKER - ID_SHIFT);
        sortingUpgrade = new ItemSortingUpgrade(ItemIds.SORTING_UPGRADE - ID_SHIFT);
        playerRelocator = new ItemPlayerRelocator(ItemIds.PLAYER_RELOCATOR - ID_SHIFT);
        relocatorModule = new ItemRelocatorModule(ItemIds.RELOCATOR_MODULE - ID_SHIFT);
        toolBox = new ItemToolBox(ItemIds.TOOLBOX - ID_SHIFT);
        frozenLiquid = new ItemFrozenLiquid(ItemIds.FROZEN_LIQUID - ID_SHIFT);

        ModObjects.linker = new ItemStack(linker);
        ModObjects.sortingUpgrade = new ItemStack(sortingUpgrade);
        ModObjects.playerRelocator = new ItemStack(playerRelocator);
        ModObjects.relocatorFilter = new ItemStack(relocatorModule);
        ModObjects.toolbox = new ItemStack(toolBox);
        ModObjects.frozenLiquid = new ItemStack(frozenLiquid);

        GameRegistry.registerItem(linker, Names.linker);
        GameRegistry.registerItem(sortingUpgrade, Names.sortingUpgrade);
        GameRegistry.registerItem(playerRelocator, Names.playerRelocator);
        GameRegistry.registerItem(relocatorModule, Names.relocatorModule);
        GameRegistry.registerItem(toolBox, Names.toolbox);
        GameRegistry.registerItem(frozenLiquid, Names.frozenLiquid);

        GameRegistry.addShapedRecipe(new ItemStack(linker), "iri", "rer", "iri", 'i', Item.ingotIron, 'r', Item.redstone, 'e', Item.enderPearl);
        GameRegistry.addShapedRecipe(new ItemStack(sortingUpgrade), "g g", " p ", "g g", 'g', Item.ingotGold, 'p', Block.thinGlass);

        if (!Settings.DISABLE_PLAYER_RELOCATOR)
        {
            GameRegistry.addShapedRecipe(new ItemStack(playerRelocator), "gbg", "ede", "gfg", 'g', Item.ingotGold, 'b', Item.blazeRod, 'e', Item.enderPearl, 'd', Item.diamond, 'f', Item.fireballCharge);
        }

        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 4, 0), "ibi", "b b", "ibi", 'i', Item.ingotIron, 'b', Block.fenceIron);
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 1), "g g", " r ", "g g", 'g', Item.ingotGold, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 2), "b b", " r ", "b b", 'b', Block.fenceIron, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 3), "b b", " r ", "b b", 'b', Block.blockRedstone, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 4), "b b", " r ", "b b", 'b', Block.blockRedstone, 'r', new ItemStack(relocatorModule, 1, 2));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 4), "b b", " r ", "b b", 'b', Block.fenceIron, 'r', new ItemStack(relocatorModule, 1, 3));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 5), "l l", " r ", "l l", 'l', new ItemStack(Item.dyePowder, 1, 4), 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 6), "e e", " r ", "e e", 'e', Item.enderPearl, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 7), "c d", " r ", "d c", 'c', Item.comparator, 'd', Item.redstone, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(relocatorModule, 1, 8), "g g", " r ", "g g", 'g', Item.glowstone, 'r', new ItemStack(relocatorModule, 1, 0));
        GameRegistry.addRecipe(new RecipeToolbox());
    }
}
