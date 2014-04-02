package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.util.BlockAndMeta;
import com.dynious.refinedrelocation.util.MultiBlockAndMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiBlockMap
{
    private Object[][][] map;

    public MultiBlockMap(int sizeX, int sizeY, int sizeZ)
    {
        map = new Object[sizeX][sizeY][sizeZ];
    }

    public void addBlockAtPos(int x, int y, int z, BlockAndMeta blockAndMeta)
    {
        if (x >= 0 && x < map.length && y >= 0 && y < map[0].length && z >= 0 && z < map[0][0].length)
        {
            if (map[x][y][z] == null)
            {
                map[x][y][z] = blockAndMeta;
            }
            else if (map[x][y][z] instanceof MultiBlockAndMeta)
            {
                ((MultiBlockAndMeta) map[x][y][z]).add(blockAndMeta);
            }
            else
            {
                map[x][y][z] = new MultiBlockAndMeta(Arrays.asList((BlockAndMeta) map[x][y][z], blockAndMeta));
            }
        }
    }

    public void addBlockAtPos(int x, int y, int z, int blockID)
    {
        addBlockAtPos(x, y, z, new BlockAndMeta(blockID));
    }

    public void addBlocksAtPos(int x, int y, int z, BlockAndMeta... blockAndMeta)
    {
        if (x >= 0 && x < map.length && y >= 0 && y < map[0].length && z >= 0 && z < map[0][0].length)
        {
            if (map[x][y][z] == null)
            {
                map[x][y][z] = new MultiBlockAndMeta(Arrays.asList(blockAndMeta));
            }
            else if (map[x][y][z] instanceof MultiBlockAndMeta)
            {
                ((MultiBlockAndMeta) map[x][y][z]).add(Arrays.asList(blockAndMeta));
            }
            else
            {
                List<BlockAndMeta> l = new ArrayList<BlockAndMeta>(Arrays.asList(blockAndMeta));
                l.add((BlockAndMeta) map[x][y][z]);
                map[x][y][z] = new MultiBlockAndMeta(l);
            }
        }
    }

    public Object getBlockAndMetaAtPos(int x, int y, int z)
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
