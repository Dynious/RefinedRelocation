package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.part.ItemPartRelocator;
import com.dynious.refinedrelocation.part.PartFactory;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FMPHelper
{
    public static Item partRelocator;

    public static void addFMPBlocks()
    {
        PartFactory.init();
        partRelocator = new ItemPartRelocator();
        GameRegistry.registerItem(partRelocator, Names.relocator);
        ModObjects.relocator = new ItemStack(partRelocator);
    }
    public static void addFMPRecipes()
    {
        GameRegistry.addShapedRecipe(new ItemStack(partRelocator, 4, 0), "igi", "g g", "igi", 'i', Items.iron_ingot, 'g', Blocks.glass_pane);
    }
}
