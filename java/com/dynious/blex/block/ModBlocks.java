package com.dynious.blex.block;

import com.dynious.blex.item.ItemBlockExtender;
import com.dynious.blex.lib.BlockIds;
import com.dynious.blex.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks
{
    public static BlockExtender blockExtender;

    public static void init()
    {
        blockExtender = new BlockExtender(BlockIds.BLOCK_EXTENDER);

        GameRegistry.registerBlock(blockExtender, ItemBlockExtender.class, Names.blockExtender);
    }
}
