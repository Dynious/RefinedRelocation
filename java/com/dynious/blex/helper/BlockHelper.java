package com.dynious.blex.helper;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BlockHelper
{
    public static final String nullBlockString = "<NONE>";

    public static String getBlockDisplayName(World world, int x, int y, int z)
    {
        int blockID = world.getBlockId(x, y, z);
        Block block = Block.blocksList[blockID];
        if (block != null)
        {
            try
            {
                ItemStack pickedItemStack = block.getPickBlock(null, world, x, y, z);
                if (pickedItemStack != null)
                    return getItemStackDisplayName(pickedItemStack);
            } catch (Exception e)
            {
                // TODO: Add proper support for using getPickBlock on multiparts
            }

            List<ItemStack> dropped = block.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            if (!dropped.isEmpty())
            {
                Collections.sort(dropped, new Comparator<ItemStack>()
                {
                    @Override
                    public int compare(ItemStack stack0, ItemStack stack1)
                    {
                        return stack1.getItemDamage() - stack0.getItemDamage();
                    }
                });
                return getItemStackDisplayName(dropped.get(0));
            }

            return getBlockDisplayName(block, world.getBlockMetadata(x, y, z));
        }

        return nullBlockString;
    }

    public static String getTileEntityDisplayName(TileEntity tile)
    {
        if (tile != null)
        {
            return getBlockDisplayName(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
        }

        return nullBlockString;
    }

    public static String getBlockDisplayName(Block block, int blockMeta)
    {
        if (block != null)
        {
            ItemStack itemstack = new ItemStack(block, 1, blockMeta);
            return getItemStackDisplayName(itemstack);
        }

        return nullBlockString;
    }

    public static String getItemStackDisplayName(ItemStack itemstack)
    {
        if (itemstack != null && itemstack.getItem() != null)
        {
            return itemstack.getDisplayName();
        }

        return nullBlockString;
    }
}
