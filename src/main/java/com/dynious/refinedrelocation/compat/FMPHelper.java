package com.dynious.refinedrelocation.compat;

import codechicken.multipart.PartMap;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.compat.part.ItemPartRelocator;
import com.dynious.refinedrelocation.compat.part.PartFactory;
import com.dynious.refinedrelocation.compat.part.PartRelocator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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

    public static void updateBlock(World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileMultipart)
        {
            TMultiPart part = ((TileMultipart) tile).partMap(PartMap.CENTER.i);
            if (part instanceof PartRelocator)
            {
                part.sendDescUpdate();
            }
        }
    }
}
