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

    public static final int ID_SHIFT = 256;

    public static void init()
    {
        linker = new ItemLinker(ItemIds.LINKER - ID_SHIFT);
        sortingUpgrade = new ItemSortingUpgrade(ItemIds.SORTING_UPGRADE - ID_SHIFT);
        playerRelocator = new ItemPlayerRelocator(ItemIds.PLAYER_RELOCATOR - ID_SHIFT);
        relocatorModule = new ItemRelocatorModule(ItemIds.RELOCATOR_MODULE - ID_SHIFT);

        ModObjects.linker = new ItemStack(linker);
        ModObjects.sortingUpgrade = new ItemStack(sortingUpgrade);
        ModObjects.playerRelocator = new ItemStack(playerRelocator);
        ModObjects.relocatorFilter = new ItemStack(relocatorModule);

        GameRegistry.registerItem(linker, Names.linker);
        GameRegistry.registerItem(sortingUpgrade, Names.sortingUpgrade);
        GameRegistry.registerItem(playerRelocator, Names.playerRelocator);
        GameRegistry.registerItem(relocatorModule, Names.relocatorModule);

        GameRegistry.addShapedRecipe(new ItemStack(linker), "iri", "rer", "iri", 'i', Item.ingotIron, 'r', Item.redstone, 'e', Item.enderPearl);
        GameRegistry.addShapedRecipe(new ItemStack(sortingUpgrade), "g g", " p ", "g g", 'g', Item.ingotGold, 'p', Block.thinGlass);

        if (!Settings.DISABLE_PLAYER_RELOCATOR)
        {
            GameRegistry.addShapedRecipe(new ItemStack(playerRelocator), "gbg", "ede", "gfg", 'g', Item.ingotGold, 'b', Item.blazeRod, 'e', Item.enderPearl, 'd', Item.diamond, 'f', Item.fireballCharge);
        }
    }
}
