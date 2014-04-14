package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.block.BlockSortingBarrel;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileSortingBarrel;
import cpw.mods.fml.common.registry.GameRegistry;
import mcp.mobius.betterbarrels.BetterBarrels;
import mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class JabbaHelper
{
    public static void addBarrelBlock()
    {
        ModBlocks.sortingBarrel = new BlockSortingBarrel(BlockIds.SORTING_BARREL);
        GameRegistry.registerBlock(ModBlocks.sortingBarrel, Names.sortingBarrel);
    }

    public static void addBarrelRecipes()
    {
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.sortingBarrel), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(BetterBarrels.blockBarrel));
    }

    public static boolean upgradeToSortingBarrel(TileEntity tile)
    {
        if (tile instanceof TileEntityBarrel && !(tile instanceof TileSortingBarrel))
        {
            World world = tile.getWorldObj();

            TileEntityBarrel oldBarrel = (TileEntityBarrel) tile;
            int meta = oldBarrel.getBlockMetadata();
            TileSortingBarrel newBarrel = new TileSortingBarrel();
            NBTTagCompound tag = new NBTTagCompound();
            oldBarrel.writeToNBT(tag);
            newBarrel.readFromNBT(tag);
            // Clear the old block out
            world.setBlockToAir(tile.xCoord, tile.yCoord, tile.zCoord);
            // And put in our block instead
            world.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, ModBlocks.sortingBarrel.blockID, meta, 3);

            world.setBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord, newBarrel);
            return true;
        }
        return false;
    }
}
