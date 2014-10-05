package com.dynious.refinedrelocation.util;

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

    public BlockAndMeta(Block block)
    {
        this.block = block;
        this.meta = -1;
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
