package com.dynious.refinedrelocation.tileentity;

import net.minecraft.block.Block;

public interface IDisguisable
{
    public boolean canDisguise();

    public boolean canDisguiseAs(Block block, int metadata);

    public Block getDisguise();

    public int getDisguiseMeta();

    public void setDisguise(Block block, int metadata);

    public void clearDisguise();
}
