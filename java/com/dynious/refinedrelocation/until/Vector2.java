package com.dynious.refinedrelocation.until;

import net.minecraft.block.Block;

public class Vector2
{
    private Block block;
    private int meta;

    public Vector2(Block block, int meta)
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
