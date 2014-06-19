package com.dynious.refinedrelocation.helper;

import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BlockHelper
{
    public static final String nullBlockString = StatCollector.translateToLocal(Strings.NONE);

    public static String getBlockDisplayName(World world, int x, int y, int z, ForgeDirection side)
    {
        Block block = world.getBlock(x, y, z);
        if (block != null)
        {
            // trace from the middle of the given side to the opposite side
            Vec3 midpos = world.getWorldVec3Pool().getVecFromPool(x+0.5D, y+0.5D, z+0.5D);
            ForgeDirection opposite = side.getOpposite();
            Vec3 startpos = midpos.addVector(opposite.offsetX*.5, opposite.offsetY*.5, opposite.offsetZ*.5);
            Vec3 endpos = midpos.addVector(side.offsetX*.5, side.offsetY*.5, side.offsetZ*.5);
            MovingObjectPosition hit = world.rayTraceBlocks(startpos, endpos);
            if (hit != null)
            {
                try
                {
                    ItemStack pickedItemStack = block.getPickBlock(hit, world, x, y, z);
                    if (pickedItemStack != null)
                        return getItemStackDisplayName(pickedItemStack);
                }
                catch(Exception e)
                {
                    // safety for multipart reduceMOP exceptions
                }
            }
        }

        return getBlockDisplayName(world, x, y, z);
    }

    public static String getBlockDisplayName(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        if (block != null)
        {
            // trace from one corner to the other so that we can be fairly certain we at least hit something
            // note that this may ignore tiny multiparts (nooks/corners)
            Vec3 startpos = world.getWorldVec3Pool().getVecFromPool(x, y, z);
            Vec3 endpos = world.getWorldVec3Pool().getVecFromPool(x+1, y+1, z+1);
            MovingObjectPosition hit = world.rayTraceBlocks(startpos, endpos);
            if (hit != null)
            {
                try
                {
                    ItemStack pickedItemStack = block.getPickBlock(hit, world, x, y, z);
                    if (pickedItemStack != null)
                        return getItemStackDisplayName(pickedItemStack);
                }
                catch(Exception e)
                {
                    // safety for multipart reduceMOP exceptions
                }
            }

            List<ItemStack> dropped = block.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            if (dropped != null && !dropped.isEmpty())
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
            return getBlockDisplayName(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
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
