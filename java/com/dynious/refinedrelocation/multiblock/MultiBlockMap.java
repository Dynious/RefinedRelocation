package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.until.BlockAndMeta;
import net.minecraft.block.Block;

public class MultiBlockMap
{
    private BlockAndMeta[][][] map;

    public MultiBlockMap(int sizeX, int sizeY, int sizeZ)
    {
        map = new BlockAndMeta[sizeX][sizeY][sizeZ];
    }

    public void addBlockAtPos(BlockAndMeta blockIdMeta, int x, int y, int z)
    {
        if (x >= 0 && x < map.length && y >= 0 && y < map[0].length && z >= 0 && z < map[0][0].length)
        {
            map[x][y][z] = blockIdMeta;
        }
    }

    public void addBlockAtPos(Block blockID, int x, int y, int z)
    {
        addBlockAtPos(new BlockAndMeta(blockID, -1), x, y, z);
    }

    public void addBlockAtPos(Block blockID, int blockMeta, int x, int y, int z)
    {
        addBlockAtPos(new BlockAndMeta(blockID, blockMeta), x, y, z);
    }

    public BlockAndMeta getBlockAtPos(int x, int y, int z)
    {
        if (x >= 0 && x < map.length && y >= 0 && y < map[0].length && z >= 0 && z < map[0][0].length)
        {
            return map[x][y][z];
        }
        return null;
    }

    public int getSizeX()
    {
        return map.length;
    }

    public int getSizeY()
    {
        return map[0].length;
    }

    public int getSizeZ()
    {
        return map[0][0].length;
    }
}
