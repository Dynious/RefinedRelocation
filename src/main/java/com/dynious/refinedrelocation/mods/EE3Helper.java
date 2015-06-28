package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.block.BlockSortingAlchemicalChest;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.client.renderer.ItemRendererSortingAlchemicalChest;
import com.dynious.refinedrelocation.client.renderer.RendererSortingAlchemicalChest;
import com.dynious.refinedrelocation.item.ItemBlockSortingAlchemicalChest;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.tileentity.TileSortingAlchemicalChest;
import com.dynious.refinedrelocation.tileentity.TileSortingChest;
import com.pahimar.ee3.init.ModItems;
import com.pahimar.ee3.item.ItemAlchemicalInventoryUpgrade;
import com.pahimar.ee3.item.ItemBlockAlchemicalChest;
import com.pahimar.ee3.tileentity.TileEntityAlchemicalChest;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;

@Deprecated
public class EE3Helper
{
    public static void addEE3Blocks()
    {
        ModBlocks.sortingAlchemicalChest = new BlockSortingAlchemicalChest();
        ModObjects.sortingAlchemicalChest = new ItemStack(ModBlocks.sortingAlchemicalChest);
        GameRegistry.registerBlock(ModBlocks.sortingAlchemicalChest, ItemBlockSortingAlchemicalChest.class, Names.sortingAlchemicalChest);
    }

    public static void addEE3Recipes()
    {
        for (int i = 0; i < 3; i++)
        {
            GameRegistry.addShapelessRecipe(new ItemStack(com.pahimar.ee3.init.ModBlocks.alchemicalChest, 1, i), new ItemStack(ModBlocks.sortingAlchemicalChest, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void addEE3Renders()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileSortingAlchemicalChest.class, new RendererSortingAlchemicalChest());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.sortingAlchemicalChest), new ItemRendererSortingAlchemicalChest());
    }

}
