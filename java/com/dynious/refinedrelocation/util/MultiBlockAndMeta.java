package com.dynious.refinedrelocation.util;

import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockAndMeta
{
    private List<BlockAndMeta> blockAndMetas;

    public MultiBlockAndMeta(Block block, int meta)
    {
        blockAndMetas = new ArrayList<BlockAndMeta>();
        blockAndMetas.add(new BlockAndMeta(block, meta));
    }

    public MultiBlockAndMeta(BlockAndMeta blockAndMeta)
    {
        blockAndMetas = new ArrayList<BlockAndMeta>();
        blockAndMetas.add(blockAndMeta);
    }

    public MultiBlockAndMeta(List<BlockAndMeta> blockAndMeta)
    {
        blockAndMetas = new ArrayList<BlockAndMeta>(blockAndMeta);
    }

    public void add(Block block, int meta)
    {
        blockAndMetas.add(new BlockAndMeta(block, meta));
    }

    public void add(BlockAndMeta blockAndMeta)
    {
        blockAndMetas.add(blockAndMeta);
    }

    public void add(List<BlockAndMeta> blockAndMeta)
    {
        blockAndMetas.addAll(blockAndMeta);
    }

    public List<BlockAndMeta> getBlockAndMetas()
    {
        return blockAndMetas;
    }
}
