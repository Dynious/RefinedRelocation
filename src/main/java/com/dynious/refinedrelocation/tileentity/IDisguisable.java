package com.dynious.refinedrelocation.tileentity;

import net.minecraft.block.Block;

public interface IDisguisable {
    boolean canDisguise();

    boolean canDisguiseAs(Block block, int metadata);

    Block getDisguise();

    int getDisguiseMeta();

    void setDisguise(Block block, int metadata);

    void clearDisguise();
}
