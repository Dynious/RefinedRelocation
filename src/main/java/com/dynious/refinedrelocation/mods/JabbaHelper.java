package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.block.BlockSortingBarrel;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.tileentity.TileSortingBarrel;
import com.dynious.refinedrelocation.tileentity.TileSortingChest;
import cpw.mods.fml.common.registry.GameRegistry;
import mcp.mobius.betterbarrels.BetterBarrels;
import mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class JabbaHelper {

    /**
     * JABBA does not provide an API for this and doesn't seem to care, so let's fix things ourselves.
     * Still won't fix rotations since JABBA is totally into hardcoding things.
     */
    @SuppressWarnings("unchecked")
    public static void enableDolly() {
        try {
            RefinedRelocation.logger.info("Attempting to patch JABBA dolly to allow picking up Sorting Chests...");
            Class itemBarrelMover = Class.forName("mcp.mobius.betterbarrels.common.items.dolly.ItemBarrelMover");
            Field classExtensionNamesField = itemBarrelMover.getDeclaredField("classExtensionsNames");
            classExtensionNamesField.setAccessible(true);
            Field classExtensionsField = itemBarrelMover.getDeclaredField("classExtensions");
            classExtensionsField.setAccessible(true);
            Field classMapField = itemBarrelMover.getDeclaredField("classMap");
            classMapField.setAccessible(true);
            ArrayList<Class> classExtensions = (ArrayList<Class>) classExtensionsField.get(null);
            ArrayList<String> classExtensionNames = (ArrayList<String>) classExtensionNamesField.get(null);
            HashMap<String, Class> classMap = (HashMap<String, Class>) classMapField.get(null);
            // Just in case JABBA adds support in the future, check first
            if(!classExtensionNames.contains(TileSortingChest.class.getName())) {
                classExtensionNames.add(TileSortingChest.class.getName());
                classExtensions.add(TileSortingChest.class);
                classMap.put(TileSortingChest.class.getName(), TileSortingChest.class);
            }
            RefinedRelocation.logger.info("JABBA dolly should be fixed and work on Sorting Chests now!");
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            RefinedRelocation.logger.error("Could not patch JABBA dolly; internal implementation might have changed. Dolly won't work on Sorting Chest.", e);
        }
    }

    public static void addBarrelBlock() {
        ModBlocks.sortingBarrel = new BlockSortingBarrel();
        ModObjects.sortingBarrel = new ItemStack(ModBlocks.sortingBarrel);
        GameRegistry.registerBlock(ModBlocks.sortingBarrel, Names.sortingBarrel);
    }

    public static void addBarrelRecipes() {
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.sortingBarrel), "g g", " b ", "g g", 'g', Items.gold_nugget, 'b', new ItemStack(BetterBarrels.blockBarrel));

        if (!Settings.DISABLE_SORTING_TO_NORMAL) {
            GameRegistry.addShapelessRecipe(new ItemStack(BetterBarrels.blockBarrel), new ItemStack(ModBlocks.sortingBarrel));
        }
    }

    public static boolean upgradeToSortingBarrel(TileEntity tile) {
        if (tile instanceof TileEntityBarrel && !(tile instanceof TileSortingBarrel)) {
            World world = tile.getWorldObj();

            TileEntityBarrel oldBarrel = (TileEntityBarrel) tile;
            int meta = oldBarrel.getBlockMetadata();
            TileSortingBarrel newBarrel = new TileSortingBarrel();
            NBTTagCompound tag = new NBTTagCompound();
            oldBarrel.writeToNBT(tag);
            newBarrel.readFromNBT(tag);
            // Clear the old block out
            world.removeTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
            world.setBlockToAir(tile.xCoord, tile.yCoord, tile.zCoord);
            // And put in our block instead
            world.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, ModBlocks.sortingBarrel, meta, 3);

            world.setTileEntity(tile.xCoord, tile.yCoord, tile.zCoord, newBarrel);
            return true;
        }
        return false;
    }

    public static boolean removeSortingUpgradeFromBarrel(TileSortingBarrel tile) {
        World world = tile.getWorldObj();
        int meta = tile.getBlockMetadata();
        TileEntityBarrel newBarrel = new TileEntityBarrel();
        NBTTagCompound tag = new NBTTagCompound();
        tile.writeToNBT(tag);
        newBarrel.readFromNBT(tag);
        // Clear the old block out
        world.removeTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
        world.setBlockToAir(tile.xCoord, tile.yCoord, tile.zCoord);
        // And put in Jabba's block instead
        world.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, BetterBarrels.blockBarrel, meta, 3);
        world.setTileEntity(tile.xCoord, tile.yCoord, tile.zCoord, newBarrel);
        return true;
    }

}
