package com.dynious.refinedrelocation.helper;

import com.dynious.refinedrelocation.tileentity.ILoopable;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoopHelper
{
    public static boolean isLooping(ILoopable owner, TileEntity tile)
    {
        return tile != null && tile instanceof ILoopable && isTileConnectedToThis((ILoopable) tile, new ArrayList<ILoopable>(Arrays.asList(owner)));
    }

    private static boolean isTileConnectedToThis(ILoopable loopable, List<ILoopable> visited)
    {
        List<TileEntity> list = loopable.getConnectedTiles();
        for (TileEntity tile : list)
        {
            if (tile == null)
                continue;
            boolean isLooping;
            if (visited.contains(tile))
            {
                return true;
            }
            if (tile instanceof ILoopable)
            {
                visited.add((ILoopable) tile);
                isLooping = isTileConnectedToThis((ILoopable) tile, visited);
            }
            else
            {
                return false;
            }
            if (isLooping)
                return true;
        }
        return false;
    }
}
