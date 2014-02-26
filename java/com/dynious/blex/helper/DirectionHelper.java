package com.dynious.blex.helper;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class DirectionHelper
{
    public static TileEntity getTileAtSide(IBlockAccess world, int x, int y, int z, ForgeDirection direction)
    {
        return world.getBlockTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
    }

    public static TileEntity getTileAtSide(TileEntity tileEntity, ForgeDirection direction)
    {
        return getTileAtSide(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, direction);
    }
}
