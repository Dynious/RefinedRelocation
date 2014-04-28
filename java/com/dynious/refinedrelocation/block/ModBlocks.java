package com.dynious.refinedrelocation.block;


import com.dynious.refinedrelocation.api.BlockObj;
import com.dynious.refinedrelocation.item.ItemBlockExtender;
import com.dynious.refinedrelocation.item.ItemBuffer;
import com.dynious.refinedrelocation.item.ItemPowerLimiter;
import com.dynious.refinedrelocation.item.ItemSortingConnector;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.mods.IronChestHelper;
import com.dynious.refinedrelocation.mods.JabbaHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModBlocks
{

    public static void init()
    {
        BlockObj.blockExtender = new BlockExtender(BlockIds.BLOCK_EXTENDER);
        BlockObj.buffer = new BlockBuffer(BlockIds.BUFFER);
        BlockObj.sortingChest = new BlockSortingChest(BlockIds.SORTING_CHEST);
        BlockObj.sortingConnector = new BlockSortingConnector(BlockIds.SORTING_CONNECTOR);
        BlockObj.filteringHopper = new BlockFilteringHopper(BlockIds.FILTERING_HOPPER);
        BlockObj.relocationPortal = new BlockRelocationPortal(BlockIds.RELOCATION_PORTAL);
        BlockObj.relocationController = new BlockRelocationController(BlockIds.RELOCATION_CONTROLLER);
        BlockObj.powerLimiter = new BlockPowerLimiter(BlockIds.POWER_LIMITER);

        GameRegistry.registerBlock(BlockObj.blockExtender, ItemBlockExtender.class, Names.blockExtender);
        GameRegistry.registerBlock(BlockObj.buffer, ItemBuffer.class, Names.buffer);
        GameRegistry.registerBlock(BlockObj.sortingChest, Names.sortingChest);
        GameRegistry.registerBlock(BlockObj.sortingConnector, ItemSortingConnector.class, Names.sortingConnector);
        GameRegistry.registerBlock(BlockObj.filteringHopper, Names.filteringHopper);
        GameRegistry.registerBlock(BlockObj.relocationPortal, Names.relocationPortal);
        GameRegistry.registerBlock(BlockObj.relocationController, Names.relocationController);
        GameRegistry.registerBlock(BlockObj.powerLimiter, ItemPowerLimiter.class, Names.powerLimiter);

        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.blockExtender, 4, 0), "igi", "geg", "ioi", 'i', Item.ingotIron, 'o', Block.obsidian, 'g', Block.thinGlass, 'e', Item.enderPearl);
        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.blockExtender, 1, 1), "r r", " b ", "r r", 'r', Block.blockRedstone, 'b', new ItemStack(BlockObj.blockExtender, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.blockExtender, 1, 2), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(BlockObj.blockExtender, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.blockExtender, 1, 3), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(BlockObj.blockExtender, 1, 1));
        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.blockExtender, 1, 3), "r r", " b ", "r r", 'r', Block.blockRedstone, 'b', new ItemStack(BlockObj.blockExtender, 1, 2));

        if (!Settings.DISABLE_WIRELESS_BLOCK_EXTENDER)
        {
            GameRegistry.addShapedRecipe(new ItemStack(BlockObj.blockExtender, 1, 4), "d d", " b ", "d d", 'd', Item.diamond, 'b', new ItemStack(BlockObj.blockExtender, 1, 3));
        }

        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.buffer, 4, 0), "igi", "geg", "igi", 'i', Item.ingotIron, 'g', Block.thinGlass, 'e', Item.enderPearl);
        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.buffer, 1, 1), "r r", " b ", "r r", 'r', Block.blockRedstone, 'b', new ItemStack(BlockObj.buffer, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.buffer, 1, 2), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(BlockObj.buffer, 1, 0));

        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.sortingChest, 1, 0), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(Block.chest));
        GameRegistry.addShapelessRecipe(new ItemStack(Block.chest), new ItemStack(BlockObj.sortingChest, 1, 0));

        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.sortingConnector, 4, 0), "gsg", "sis", "gsg", 'g', Item.goldNugget, 's', Block.stone, 'i', Item.ingotIron);
        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.sortingConnector, 1, 1), "g g", " i ", "g g", 'g', Item.ingotGold, 'i', new ItemStack(BlockObj.sortingConnector, 4, 0));
        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.sortingConnector, 1, 2), "rgr", "sis", "rgr", 'g', Item.ingotGold, 's', Item.redstone, 'r', Item.ingotIron, 'i', new ItemStack(BlockObj.sortingConnector, 4, 0));

        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.filteringHopper), "g g", " h ", "g g", 'g', Item.ingotGold, 'h', new ItemStack(Block.hopperBlock));
        GameRegistry.addShapedRecipe(new ItemStack(BlockObj.powerLimiter), "iri", "rbr", "iri", 'i', Item.ingotIron, 'r', Item.redstone, 'b', Block.blockRedstone);

        if (!Settings.DISABLE_PLAYER_RELOCATOR)
        {
            GameRegistry.addShapedRecipe(new ItemStack(BlockObj.relocationController), "ded", "ece", "ded", 'd', Item.diamond, 'e', Item.eyeOfEnder, 'c', Item.compass);
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
        BlockObj.isHere = true;
    }
}
