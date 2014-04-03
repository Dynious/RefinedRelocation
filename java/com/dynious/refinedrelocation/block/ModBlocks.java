package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.item.ItemBlockExtender;
import com.dynious.refinedrelocation.item.ItemBuffer;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.mods.IronChestHelper;
import com.dynious.refinedrelocation.mods.JabbaHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

    public static void init()
    {
        blockExtender = new BlockExtender();
        buffer = new BlockBuffer();
        sortingChest = new BlockSortingChest();
        sortingConnector = new BlockSortingConnector();
        filteringHopper = new BlockFilteringHopper();
        relocationPortal = new BlockRelocationPortal();
        relocationController = new BlockRelocationController();

        GameRegistry.registerBlock(blockExtender, ItemBlockExtender.class, Names.blockExtender);
        GameRegistry.registerBlock(buffer, ItemBuffer.class, Names.buffer);
        GameRegistry.registerBlock(sortingChest, Names.sortingChest);
        GameRegistry.registerBlock(sortingConnector, Names.sortingConnector);
        GameRegistry.registerBlock(filteringHopper, Names.filteringHopper);
        GameRegistry.registerBlock(relocationPortal, Names.relocationPortal);
        GameRegistry.registerBlock(relocationController, Names.relocationController);

        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 4, 0), "igi", "geg", "ioi", 'i', Items.iron_ingot, 'o', Blocks.obsidian, 'g', Blocks.glass_pane, 'e', Items.ender_pearl);
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 1), "r r", " b ", "r r", 'r', Blocks.redstone_block, 'b', new ItemStack(blockExtender, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 2), "g g", " b ", "g g", 'g', Items.gold_ingot, 'b', new ItemStack(blockExtender, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 3), "g g", " b ", "g g", 'g', Items.gold_ingot, 'b', new ItemStack(blockExtender, 1, 1));
        GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 3), "r r", " b ", "r r", 'r', Blocks.redstone_block, 'b', new ItemStack(blockExtender, 1, 2));

        if (!Settings.DISABLE_WIRELESS_BLOCK_EXTENDER)
        {
            GameRegistry.addShapedRecipe(new ItemStack(blockExtender, 1, 4), "d d", " b ", "d d", 'd', Items.diamond, 'b', new ItemStack(blockExtender, 1, 3));
        }

        GameRegistry.addShapedRecipe(new ItemStack(buffer, 4, 0), "igi", "geg", "igi", 'i', Items.iron_ingot, 'g', Blocks.glass_pane, 'e', Items.ender_pearl);
        GameRegistry.addShapedRecipe(new ItemStack(buffer, 1, 1), "r r", " b ", "r r", 'r', Blocks.redstone_block, 'b', new ItemStack(buffer, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(buffer, 1, 2), "g g", " b ", "g g", 'g', Items.gold_ingot, 'b', new ItemStack(buffer, 1, 0));

        GameRegistry.addShapedRecipe(new ItemStack(sortingChest, 1, 0), "g g", " b ", "g g", 'g', Items.gold_ingot, 'b', new ItemStack(Blocks.chest));
        GameRegistry.addShapedRecipe(new ItemStack(sortingConnector, 4, 0), "gsg", "sis", "gsg", 'g', Blocks.glass_pane, 's', Blocks.stone, 'i', Items.iron_ingot);

        GameRegistry.addShapedRecipe(new ItemStack(filteringHopper), "g g", " h ", "g g", 'g', Items.gold_ingot, 'h', new ItemStack(Blocks.hopper));

        GameRegistry.addShapedRecipe(new ItemStack(relocationController), "ded", "ece", "ded", 'd', Items.diamond, 'e', Items.ender_eye, 'c', Items.compass);

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
    }
}
