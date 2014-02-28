package com.dynious.blex.tileentity;

import net.minecraft.block.Block;

public interface IDisguisable
{
    public boolean canDisguise();
    
    public boolean canDisguiseAs(Block block, int metadata);

    public Block getDisguise();

    public void setDisguise(Block block, int metadata);

    public void clearDisguise();
}
