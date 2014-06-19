package com.dynious.refinedrelocation.mods;

import codechicken.multipart.PartMap;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.part.ItemPartRelocator;
import com.dynious.refinedrelocation.part.PartFactory;
import com.dynious.refinedrelocation.part.PartRelocator;
import com.dynious.refinedrelocation.renderer.ItemRendererRelocator;
import com.dynious.refinedrelocation.renderer.ItemRendererSortingAlchemicalChest;
import com.dynious.refinedrelocation.renderer.RendererSortingAlchemicalChest;
import com.dynious.refinedrelocation.tileentity.TileSortingAlchemicalChest;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class FMPHelper
{
    public static Item partRelocator;

    public static void addFMPBlocks()
    {
        PartFactory.init();
        partRelocator = new ItemPartRelocator(BlockIds.RELOCATOR - ModItems.ID_SHIFT);
        GameRegistry.registerItem(partRelocator, Names.relocator);
        ModObjects.relocator = new ItemStack(partRelocator);
    }

    public static void addFMPRecipes()
    {
        GameRegistry.addShapedRecipe(new ItemStack(partRelocator, 4, 0), "igi", "g g", "igi", 'i', Item.ingotIron, 'g', Block.thinGlass);
    }

    public static void updateBlock(World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
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
