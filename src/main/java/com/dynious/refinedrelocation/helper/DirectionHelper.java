package com.dynious.refinedrelocation.helper;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class DirectionHelper
{
    public static TileEntity getTileAtSide(IBlockAccess world, int x, int y, int z, ForgeDirection direction)
    {
        return world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
    }

    public static TileEntity getTileAtSide(TileEntity tileEntity, ForgeDirection direction)
    {
        return getTileAtSide(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, direction);
    }

    public static Block getBlockAtSide(IBlockAccess world, int x, int y, int z, ForgeDirection direction)
    {
        return world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
    }

    public static Block getBlockAtSide(TileEntity tileEntity, ForgeDirection direction)
    {
        return getBlockAtSide(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, direction);
    }
}
