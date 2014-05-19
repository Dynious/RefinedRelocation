package com.dynious.refinedrelocation.util;

public class BlockAndMeta
{
    private int blockId, meta;

    public BlockAndMeta(int blockId)
    {
        this(blockId, -1);
    }

    public BlockAndMeta(int blockId, int meta)
    {
        this.blockId = blockId;
        this.meta = meta;
    }

    public int getBlockId()
    {
        return blockId;
    }

    public int getMeta()
    {
        return meta;
    }
}
