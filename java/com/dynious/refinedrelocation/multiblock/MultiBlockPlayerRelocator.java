package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.util.BlockAndMeta;
import com.dynious.refinedrelocation.util.Vector3;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockPlayerRelocator implements IMultiBlock
{
    private static MultiBlockMap map;

    public MultiBlockPlayerRelocator()
    {
        if (map == null)
        {
            map = new MultiBlockMap(3, 4, 3);
            initMap();
        }
    }

    private static void initMap()
    {
        map.addBlockAtPos(0, 0, 0, Block.blockRedstone.blockID);
        map.addBlockAtPos(2, 0, 0, Block.blockRedstone.blockID);
        map.addBlockAtPos(0, 0, 2, Block.blockRedstone.blockID);
        map.addBlockAtPos(2, 0, 2, Block.blockRedstone.blockID);
        map.addBlockAtPos(2, 3, 0, Block.blockRedstone.blockID);
        map.addBlockAtPos(2, 3, 2, Block.blockRedstone.blockID);
        map.addBlockAtPos(0, 3, 2, Block.blockRedstone.blockID);
        map.addBlockAtPos(0, 3, 0, Block.blockRedstone.blockID);

        map.addBlockAtPos(1, 0, 2, Block.blockIron.blockID);
        map.addBlockAtPos(1, 0, 0, Block.blockIron.blockID);
        map.addBlockAtPos(2, 0, 1, Block.blockIron.blockID);
        map.addBlockAtPos(0, 0, 1, Block.blockIron.blockID);
        map.addBlockAtPos(1, 3, 2, Block.blockIron.blockID);
        map.addBlockAtPos(1, 3, 0, Block.blockIron.blockID);
        map.addBlockAtPos(2, 3, 1, Block.blockIron.blockID);
        map.addBlockAtPos(0, 3, 1, Block.blockIron.blockID);

        map.addBlockAtPos(0, 1, 0, Block.fenceIron.blockID);
        map.addBlockAtPos(0, 1, 2, Block.fenceIron.blockID);
        map.addBlockAtPos(2, 1, 2, Block.fenceIron.blockID);
        map.addBlockAtPos(2, 1, 0, Block.fenceIron.blockID);
        map.addBlockAtPos(0, 2, 0, Block.fenceIron.blockID);
        map.addBlockAtPos(0, 2, 2, Block.fenceIron.blockID);
        map.addBlockAtPos(2, 2, 2, Block.fenceIron.blockID);
        map.addBlockAtPos(2, 2, 0, Block.fenceIron.blockID);

        map.addBlocksAtPos(1, 3, 1, new BlockAndMeta(Block.blockGold.blockID), new BlockAndMeta(Block.blockDiamond.blockID));
        map.addBlockAtPos(1, 0, 1, ModBlocks.relocationController.blockID);

        map.addBlockAtPos(1, 1, 1, 0);
        map.addBlockAtPos(1, 2, 1, 0);
    }

    @Override
    public MultiBlockMap getMultiBlockMap()
    {
        return map;
    }

    @Override
    public Vector3 getRelativeLeaderPos()
    {
        return new Vector3(1, 0, 1);
    }
}
