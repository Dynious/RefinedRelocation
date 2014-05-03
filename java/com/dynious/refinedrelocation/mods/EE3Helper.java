package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.block.BlockSortingAlchemicalChest;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.renderer.ItemRendererSortingAlchemicalChest;
import com.dynious.refinedrelocation.renderer.RendererSortingAlchemicalChest;
import com.dynious.refinedrelocation.tileentity.TileSortingAlchemicalChest;
import com.dynious.refinedrelocation.tileentity.TileSortingChest;
import com.pahimar.ee3.client.renderer.item.ItemAlchemicalChestRenderer;
import com.pahimar.ee3.item.ItemAlchemicalUpgrade;
import com.pahimar.ee3.item.ItemBlockAlchemicalChest;
import com.pahimar.ee3.tileentity.TileAlchemicalChest;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class EE3Helper
{
    public static void addEE3Blocks()
    {
        ModBlocks.sortingAlchemicalChest = new BlockSortingAlchemicalChest(BlockIds.SORTING_ALCHEMICAL_CHEST);
        ModObjects.sortingAlchemicalChest = new ItemStack(ModBlocks.sortingAlchemicalChest);
        GameRegistry.registerBlock(ModBlocks.sortingAlchemicalChest, ItemBlockAlchemicalChest.class, Names.sortingAlchemicalChest);
    }

    public static void addEE3Recipes()
    {
        for (int i = 0; i < 3; i++)
        {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.sortingAlchemicalChest, 1, i), "g g", " c ", "g g", 'g', Item.ingotGold, 'c', new ItemStack(com.pahimar.ee3.block.ModBlocks.alchemicalChest, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void addEE3Renders()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileSortingAlchemicalChest.class, new RendererSortingAlchemicalChest());
        MinecraftForgeClient.registerItemRenderer(BlockIds.SORTING_ALCHEMICAL_CHEST, new ItemRendererSortingAlchemicalChest());
    }

    public static boolean upgradeToAlchemicalChest(World world, EntityPlayer player, int x, int y, int z)
    {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemAlchemicalUpgrade)
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile instanceof TileSortingChest)
            {
                TileSortingChest oldChest = (TileSortingChest) tile;
                if (oldChest.numUsingPlayers > 0)
                {
                    return false;
                }
                // Force old TE out of the world so that adjacent chests can update
                TileSortingAlchemicalChest chest = new TileSortingAlchemicalChest(player.getHeldItem().getItemDamage());
                chest.setOrientation((byte) oldChest.getFacing());
                for (int i = 0; i < oldChest.getSizeInventory(); i++)
                {
                    chest.putStackInSlot(oldChest.getStackInSlot(i), i);
                    oldChest.putStackInSlot(null, i);
                }

                NBTTagCompound tag = new NBTTagCompound();
                oldChest.getFilter().writeToNBT(tag);
                chest.getFilter().readFromNBT(tag);

                // Clear the old block out
                world.setBlock(x, y, z, 0, 0, 3);
                // Force the Chest TE to reset it's knowledge of neighbouring blocks
                // And put in our block instead
                world.setBlock(x, y, z, ModBlocks.sortingAlchemicalChest.blockID, player.getHeldItem().getItemDamage(), 3);

                world.setBlockTileEntity(x, y, z, chest);
                world.setBlockMetadataWithNotify(x, y, z, player.getHeldItem().getItemDamage(), 3);
                player.getHeldItem().stackSize--;
                return true;
            }
        }
        return false;
    }

    public static boolean upgradeAlchemicalToSortingChest(TileEntity tile)
    {
        if (tile instanceof TileAlchemicalChest && !(tile instanceof TileSortingAlchemicalChest))
        {
            World world = tile.getWorldObj();

            TileAlchemicalChest oldChest = (TileAlchemicalChest) tile;
            int meta = oldChest.getBlockMetadata();

            int numUsers = ObfuscationReflectionHelper.getPrivateValue(TileAlchemicalChest.class, oldChest, "numUsingPlayers");
            if (numUsers > 0)
            {
                return false;
            }
            TileSortingAlchemicalChest chest = new TileSortingAlchemicalChest(tile.blockMetadata);
            for (int i = 0; i < oldChest.getSizeInventory(); i++)
            {
                chest.putStackInSlot(oldChest.getStackInSlot(i), i);
                oldChest.setInventorySlotContents(i, null);
            }

            // Clear the old block out
            world.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, 0, 0, 3);
            // And put in our block instead
            world.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, ModBlocks.sortingIronChest.blockID, meta, 3);

            world.setBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord, chest);
            world.setBlockMetadataWithNotify(tile.xCoord, tile.yCoord, tile.zCoord, meta, 3);
            return true;
        }
        return false;
    }
}
