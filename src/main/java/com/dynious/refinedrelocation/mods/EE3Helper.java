package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.block.BlockSortingAlchemicalChest;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.client.renderer.ItemRendererSortingAlchemicalChest;
import com.dynious.refinedrelocation.client.renderer.RendererSortingAlchemicalChest;
import com.dynious.refinedrelocation.item.ItemSortingUpgrade;
import com.dynious.refinedrelocation.lib.Names;
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

public class EE3Helper
{
    public static void addEE3Blocks()
    {
        ModBlocks.sortingAlchemicalChest = new BlockSortingAlchemicalChest();
        ModObjects.sortingAlchemicalChest = new ItemStack(ModBlocks.sortingAlchemicalChest);
        GameRegistry.registerBlock(ModBlocks.sortingAlchemicalChest, ItemBlockAlchemicalChest.class, Names.sortingAlchemicalChest);
    }

    public static void addEE3Recipes()
    {
        for (int i = 0; i < 3; i++)
        {
            ItemStack material = new ItemStack(ModItems.alchemicalDust, 8, i + 1);
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.sortingAlchemicalChest, 1, i), "g g", " c ", "m m", 'g', Items.gold_ingot, 'c', new ItemStack(com.pahimar.ee3.init.ModBlocks.alchemicalChest, 1, i), 'm', material);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void addEE3Renders()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileSortingAlchemicalChest.class, new RendererSortingAlchemicalChest());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.sortingAlchemicalChest), new ItemRendererSortingAlchemicalChest());
    }

    public static boolean upgradeToAlchemicalChest(World world, EntityPlayer player, int x, int y, int z)
    {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemAlchemicalInventoryUpgrade)
        {
            TileEntity tile = world.getTileEntity(x, y, z);
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
                world.setBlockToAir(x, y, z);
                // Force the Chest TE to reset it's knowledge of neighbouring blocks
                // And put in our block instead
                world.setBlock(x, y, z, ModBlocks.sortingAlchemicalChest, player.getHeldItem().getItemDamage(), 3);

                world.setTileEntity(x, y, z, chest);
                world.setBlockMetadataWithNotify(x, y, z, player.getHeldItem().getItemDamage(), 3);
                player.getHeldItem().stackSize--;
                return true;
            }
        }
        return false;
    }

    public static boolean upgradeAlchemicalToSortingChest(TileEntity tile)
    {
        if (tile instanceof TileEntityAlchemicalChest && !(tile instanceof TileSortingAlchemicalChest))
        {
            World world = tile.getWorldObj();

            TileEntityAlchemicalChest oldChest = (TileEntityAlchemicalChest) tile;
            int meta = oldChest.getBlockMetadata();
            ForgeDirection dir = oldChest.getOrientation();

            int numUsers = ObfuscationReflectionHelper.getPrivateValue(TileEntityAlchemicalChest.class, oldChest, "numUsingPlayers");

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
            chest.setOrientation(dir);

            // Clear the old block out
            world.setBlockToAir(tile.xCoord, tile.yCoord, tile.zCoord);
            // And put in our block instead
            world.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, ModBlocks.sortingAlchemicalChest, meta, 3);

            world.setTileEntity(tile.xCoord, tile.yCoord, tile.zCoord, chest);
            world.setBlockMetadataWithNotify(tile.xCoord, tile.yCoord, tile.zCoord, meta, 3);
            return true;
        }
        return false;
    }

    public static ItemStack getUpgradeItemStack(TileEntity tile)
    {
        int meta = tile.getBlockMetadata();
        return new ItemStack(ModItems.alchemicalDust, 1, meta + 1);
    }

    public static boolean isAlchemicalChest(TileEntity tile)
    {
        return tile instanceof TileEntityAlchemicalChest;
    }
}
