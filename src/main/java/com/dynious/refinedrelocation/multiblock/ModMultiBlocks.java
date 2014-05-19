package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.lib.Names;

public class ModMultiBlocks
{
    public static void init()
    {
        MultiBlockRegistry.registerMultiBlock(new MultiBlockPlayerRelocator(), Names.playerRelocator);
    }
}
