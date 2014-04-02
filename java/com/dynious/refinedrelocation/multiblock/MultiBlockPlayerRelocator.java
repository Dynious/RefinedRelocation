package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.until.BlockAndMeta;
import com.dynious.refinedrelocation.until.Vector3;
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
        map.addBlockAtPos(Blocks.redstone_block, 0, 0, 0);
        map.addBlockAtPos(Blocks.redstone_block, 2, 0, 0);
        map.addBlockAtPos(Blocks.redstone_block, 0, 0, 2);
        map.addBlockAtPos(Blocks.redstone_block, 2, 0, 2);
        map.addBlockAtPos(Blocks.redstone_block, 2, 3, 0);
        map.addBlockAtPos(Blocks.redstone_block, 2, 3, 2);
        map.addBlockAtPos(Blocks.redstone_block, 0, 3, 2);
        map.addBlockAtPos(Blocks.redstone_block, 0, 3, 0);

        map.addBlockAtPos(Blocks.iron_block, 1, 0, 2);
        map.addBlockAtPos(Blocks.iron_block, 1, 0, 0);
        map.addBlockAtPos(Blocks.iron_block, 2, 0, 1);
        map.addBlockAtPos(Blocks.iron_block, 0, 0, 1);
        map.addBlockAtPos(Blocks.iron_block, 1, 3, 2);
        map.addBlockAtPos(Blocks.iron_block, 1, 3, 0);
        map.addBlockAtPos(Blocks.iron_block, 2, 3, 1);
        map.addBlockAtPos(Blocks.iron_block, 0, 3, 1);

        map.addBlockAtPos(Blocks.iron_bars, 0, 1, 0);
        map.addBlockAtPos(Blocks.iron_bars, 0, 1, 2);
        map.addBlockAtPos(Blocks.iron_bars, 2, 1, 2);
        map.addBlockAtPos(Blocks.iron_bars, 2, 1, 0);
        map.addBlockAtPos(Blocks.iron_bars, 0, 2, 0);
        map.addBlockAtPos(Blocks.iron_bars, 0, 2, 2);
        map.addBlockAtPos(Blocks.iron_bars, 2, 2, 2);
        map.addBlockAtPos(Blocks.iron_bars, 2, 2, 0);

        map.addBlockAtPos(-1, 1, 3, 1);
        map.addBlockAtPos(ModBlocks.relocationController, 1, 0, 1);

        map.addBlockAtPos(0, 1, 1, 1);
        map.addBlockAtPos(0, 1, 2, 1);
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

    @Override
    public List<BlockAndMeta> getOptionalBlockList(int optionalId)
    {
        List<BlockAndMeta> list = new ArrayList<BlockAndMeta>();
        switch(optionalId)
        {
            case -1:
                list.add(new BlockAndMeta(Blocks.gold_block, -1));
                list.add(new BlockAndMeta(Blocks.diamond_block, -1));
                break;
        }
        return list;
    }
}
