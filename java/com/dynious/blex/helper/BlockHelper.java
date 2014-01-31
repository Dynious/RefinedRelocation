package com.dynious.blex.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHelper
{
    public static String getBlockDisplayName(World world, int x, int y, int z)
    {
        return getTileEntityDisplayName(world.getBlockTileEntity(x, y, z));
    }

    public static String getTileEntityDisplayName(TileEntity tile)
    {
        String displayName = "<NULL>";

        if (tile != null)
        {
            ItemStack itemstack = new ItemStack(tile.getBlockType(), 0, tile.getBlockMetadata());
            displayName = itemstack.getDisplayName();
        }

        return displayName;
    }
}
