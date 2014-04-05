package com.dynious.refinedrelocation.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiBlockAndMeta
{
    private List<BlockAndMeta> blockAndMetas;

    public MultiBlockAndMeta(int blockId, int meta)
    {
        blockAndMetas = new ArrayList<BlockAndMeta>();
        blockAndMetas.add(new BlockAndMeta(blockId, meta));
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

    public void add(int blockId, int meta)
    {
        blockAndMetas.add(new BlockAndMeta(blockId, meta));
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
