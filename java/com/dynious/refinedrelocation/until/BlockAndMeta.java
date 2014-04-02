package com.dynious.refinedrelocation.until;

import net.minecraft.block.Block;

public class BlockAndMeta
{
    private Block block;
    private int meta;

    public BlockAndMeta(Block block, int meta)
    {
        this.block = block;
        this.meta = meta;
    }

    public Block getBlock()
    {
        return block;
    }

    public int getMeta()
    {
        return meta;
    }
}
