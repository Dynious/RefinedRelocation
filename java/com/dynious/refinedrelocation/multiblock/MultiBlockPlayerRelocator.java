package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.util.BlockAndMeta;
import com.dynious.refinedrelocation.util.Vector3;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

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
        map.addBlockAtPos(0, 0, 0, Blocks.redstone_block);
        map.addBlockAtPos(2, 0, 0, Blocks.redstone_block);
        map.addBlockAtPos(0, 0, 2, Blocks.redstone_block);
        map.addBlockAtPos(2, 0, 2, Blocks.redstone_block);
        map.addBlockAtPos(2, 3, 0, Blocks.redstone_block);
        map.addBlockAtPos(2, 3, 2, Blocks.redstone_block);
        map.addBlockAtPos(0, 3, 2, Blocks.redstone_block);
        map.addBlockAtPos(0, 3, 0, Blocks.redstone_block);

        map.addBlockAtPos(1, 0, 2, Blocks.iron_block);
        map.addBlockAtPos(1, 0, 0, Blocks.iron_block);
        map.addBlockAtPos(2, 0, 1, Blocks.iron_block);
        map.addBlockAtPos(0, 0, 1, Blocks.iron_block);
        map.addBlockAtPos(1, 3, 2, Blocks.iron_block);
        map.addBlockAtPos(1, 3, 0, Blocks.iron_block);
        map.addBlockAtPos(2, 3, 1, Blocks.iron_block);
        map.addBlockAtPos(0, 3, 1, Blocks.iron_block);

        map.addBlockAtPos(0, 1, 0, Blocks.iron_bars);
        map.addBlockAtPos(0, 1, 2, Blocks.iron_bars);
        map.addBlockAtPos(2, 1, 2, Blocks.iron_bars);
        map.addBlockAtPos(2, 1, 0, Blocks.iron_bars);
        map.addBlockAtPos(0, 2, 0, Blocks.iron_bars);
        map.addBlockAtPos(0, 2, 2, Blocks.iron_bars);
        map.addBlockAtPos(2, 2, 2, Blocks.iron_bars);
        map.addBlockAtPos(2, 2, 0, Blocks.iron_bars);

        map.addBlocksAtPos(1, 3, 1, new BlockAndMeta(Blocks.gold_block), new BlockAndMeta(Blocks.diamond_block));
        map.addBlockAtPos(1, 0, 1, ModBlocks.relocationController);

        map.addBlockAtPos(1, 1, 1, Blocks.air);
        map.addBlockAtPos(1, 2, 1, Blocks.air);
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
