package com.dynious.refinedrelocation.until;

public class BlockAndMeta
{
    private int blockId, meta;

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
