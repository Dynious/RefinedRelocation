package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.item.ItemBlockExtender;
import com.dynious.refinedrelocation.item.ItemBuffer;
import com.dynious.refinedrelocation.item.ItemPowerLimiter;
import com.dynious.refinedrelocation.item.ItemSortingConnector;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.mods.EE3Helper;
import com.dynious.refinedrelocation.mods.IronChestHelper;
import com.dynious.refinedrelocation.mods.JabbaHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModBlocks
{
    public static BlockExtender blockExtender;
    public static BlockBuffer buffer;
    public static BlockSortingChest sortingChest;
    public static BlockSortingIronChest sortingIronChest;
    public static BlockSortingConnector sortingConnector;
    public static BlockFilteringHopper filteringHopper;
    public static BlockSortingBarrel sortingBarrel;
    public static BlockRelocationPortal relocationPortal;
    public static BlockRelocationController relocationController;
    public static BlockPowerLimiter powerLimiter;
    public static BlockSortingAlchemicalChest sortingAlchemicalChest;
    public static BlockRelocator relocator;

    public static void init()
    {
        blockExtender = new BlockExtender(BlockIds.BLOCK_EXTENDER);
        buffer = new BlockBuffer(BlockIds.BUFFER);
        sortingChest = new BlockSortingChest(BlockIds.SORTING_CHEST);
        sortingConnector = new BlockSortingConnector(BlockIds.SORTING_CONNECTOR);
        filteringHopper = new BlockFilteringHopper(BlockIds.FILTERING_HOPPER);
        relocationPortal = new BlockRelocationPortal(BlockIds.RELOCATION_PORTAL);
        relocationController = new BlockRelocationController(BlockIds.RELOCATION_CONTROLLER);
        powerLimiter = new BlockPowerLimiter(BlockIds.POWER_LIMITER);
        relocator = new BlockRelocator(BlockIds.RELOCATOR);

        ModObjects.blockExtender = new ItemStack(blockExtender);
        ModObjects.advancedBlockExtender = new ItemStack(blockExtender, 1, 1);
        ModObjects.filteredBlockExtender = new ItemStack(blockExtender, 1, 2);
        ModObjects.advancedFilteredBlockExtender = new ItemStack(blockExtender, 1, 3);
        ModObjects.wirelessBlockExtender = new ItemStack(blockExtender, 1, 4);
        ModObjects.buffer = new ItemStack(buffer);
        ModObjects.advancedBuffer = new ItemStack(buffer, 1, 1);
        ModObjects.filteredBuffer = new ItemStack(buffer, 1, 2);
        ModObjects.sortingChest = new ItemStack(sortingChest);
        ModObjects.sortingConnector = new ItemStack(sortingConnector);
        ModObjects.sortingInterface = new ItemStack(sortingConnector, 1, 1);
        ModObjects.sortingImporter = new ItemStack(sortingConnector, 1, 2);
        ModObjects.filteringHopper = new ItemStack(filteringHopper);
        ModObjects.relocationPortal = new ItemStack(relocationPortal);
        ModObjects.relocationController = new ItemStack(relocationController);
        ModObjects.powerLimiter = new ItemStack(powerLimiter);
        ModObjects.relocator = new ItemStack(relocator);

        GameRegistry.registerBlock(blockExtender, ItemBlockExtender.class, Names.blockExtender);
        GameRegistry.registerBlock(buffer, ItemBuffer.class, Names.buffer);
        GameRegistry.registerBlock(sortingChest, Names.sortingChest);
        GameRegistry.registerBlock(sortingConnector, ItemSortingConnector.class, Names.sortingConnector);
        GameRegistry.registerBlock(filteringHopper, Names.filteringHopper);
        GameRegistry.registerBlock(relocationPortal, Names.relocationPortal);
        GameRegistry.registerBlock(relocationController, Names.relocationController);
        GameRegistry.registerBlock(powerLimiter, ItemPowerLimiter.class, Names.powerLimiter);
        GameRegistry.registerBlock(relocator, Names.relocator);

        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 4, 0), "igi", "geg", "ioi", 'i', Item.ingotIron, 'o', Block.obsidian, 'g', Block.thinGlass, 'e', Item.enderPearl);
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 1), "r r", " b ", "r r", 'r', Block.blockRedstone, 'b', new ItemStack(blockExtender, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 2), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(blockExtender, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 3), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(blockExtender, 1, 1));
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 3), "r r", " b ", "r r", 'r', Block.blockRedstone, 'b', new ItemStack(blockExtender, 1, 2));

        if (!Settings.DISABLE_WIRELESS_BLOCK_EXTENDER)
        {
            GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 4), "d d", " b ", "d d", 'd', Item.diamond, 'b', new ItemStack(blockExtender, 1, 3));
        }

        GameRegistry.addShapedRecipe(new ItemStack(buffer, 4, 0), "igi", "geg", "igi", 'i', Item.ingotIron, 'g', Block.thinGlass, 'e', Item.enderPearl);
        GameRegistry.addShapedRecipe(new ItemStack(buffer, 1, 1), "r r", " b ", "r r", 'r', Block.blockRedstone, 'b', new ItemStack(buffer, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(buffer, 1, 2), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(buffer, 1, 0));

        GameRegistry.addShapedRecipe(new ItemStack(sortingChest, 1, 0), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(Block.chest));
        GameRegistry.addShapelessRecipe(new ItemStack(Block.chest), new ItemStack(sortingChest, 1, 0));

        GameRegistry.addShapedRecipe(new ItemStack(sortingConnector, 4, 0), "gsg", "sis", "gsg", 'g', Item.goldNugget, 's', Block.stone, 'i', Item.ingotIron);
        GameRegistry.addShapedRecipe(new ItemStack(sortingConnector, 1, 1), "g g", " i ", "g g", 'g', Item.ingotGold, 'i', new ItemStack(sortingConnector, 4, 0));
        GameRegistry.addShapedRecipe(new ItemStack(sortingConnector, 1, 2), "rgr", "sis", "rgr", 'g', Item.ingotGold, 's', Item.redstone, 'r', Item.ingotIron, 'i', new ItemStack(sortingConnector, 4, 0));

        GameRegistry.addShapedRecipe(new ItemStack(filteringHopper), "g g", " h ", "g g", 'g', Item.ingotGold, 'h', new ItemStack(Block.hopperBlock));
        GameRegistry.addShapedRecipe(new ItemStack(powerLimiter), "iri", "rbr", "iri", 'i', Item.ingotIron, 'r', Item.redstone, 'b', Block.blockRedstone);

        if (!Settings.DISABLE_PLAYER_RELOCATOR)
        {
            GameRegistry.addShapedRecipe(new ItemStack(relocationController), "ded", "ece", "ded", 'd', Item.diamond, 'e', Item.eyeOfEnder, 'c', Item.compass);
        }

        if (Loader.isModLoaded("IronChest"))
        {
            IronChestHelper.addIronChestBlocks();
            IronChestHelper.addIronChestRecipes();
        }

        if (Loader.isModLoaded("JABBA"))
        {
            JabbaHelper.addBarrelBlock();
            JabbaHelper.addBarrelRecipes();
        }

        if (Loader.isModLoaded("EE3"))
        {
            EE3Helper.addEE3Blocks();
            EE3Helper.addEE3Recipes();
        }
    }
}
