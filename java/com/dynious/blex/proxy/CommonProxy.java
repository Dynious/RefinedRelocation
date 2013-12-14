package com.dynious.blex.proxy;

import com.dynious.blex.lib.Names;
import com.dynious.blex.tileentity.TileAdvancedBlockExtender;
import com.dynious.blex.tileentity.TileAdvancedFilteredBlockExtender;
import com.dynious.blex.tileentity.TileBlockExtender;
import com.dynious.blex.tileentity.TileFilteredBlockExtender;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public static void initTileEntities()
    {
        GameRegistry.registerTileEntity(TileBlockExtender.class, Names.blockExtender);
        GameRegistry.registerTileEntity(TileAdvancedBlockExtender.class, Names.advancedBlockExtender);
        GameRegistry.registerTileEntity(TileFilteredBlockExtender.class, Names.filteredBlockExtender);
        GameRegistry.registerTileEntity(TileAdvancedFilteredBlockExtender.class, Names.filteredAdvancedBlockExtender);
    }
}
