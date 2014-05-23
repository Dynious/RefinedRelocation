package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.block.BlockSortingPreciousChest;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.renderer.ItemRendererSortingPreciousChest;
import com.dynious.refinedrelocation.renderer.RendererSortingPreciousChest;
import com.dynious.refinedrelocation.tileentity.TileSortingPreciousChest;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import rebelkeithy.mods.metallurgy.machines.MetallurgyMachines;
import rebelkeithy.mods.metallurgy.machines.chests.ItemBlockPreciousChest;

public class MetallurgyHelper
{
    public static void addMetalBlocks()
    {
        ModBlocks.sortingPreciousChest = new BlockSortingPreciousChest(BlockIds.SORTING_PRECIOUS_CHEST);
        ModObjects.sortingPreciousChest = new ItemStack(ModBlocks.sortingPreciousChest);
        GameRegistry.registerBlock(ModBlocks.sortingPreciousChest, ItemBlockPreciousChest.class, Names.sortingPreciousChest);
    }

    public static void addMetalRecipes()
    {
        for (int i = 0; i < 5; i++)
        {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.sortingPreciousChest, 1, i), "g g", " c ", "g g", 'g', Item.ingotGold, 'c', new ItemStack(MetallurgyMachines.chest, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void addMetalRenders()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileSortingPreciousChest.class, new RendererSortingPreciousChest());
        MinecraftForgeClient.registerItemRenderer(BlockIds.SORTING_PRECIOUS_CHEST, new ItemRendererSortingPreciousChest());
    }
}
