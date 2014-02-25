package com.dynious.blex.proxy;

import com.dynious.blex.lib.Names;
import com.dynious.blex.network.GuiHandler;
import com.dynious.blex.tileentity.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public void initTileEntities()
    {
        GameRegistry.registerTileEntity(TileBlockExtender.class, Names.blockExtender);
        GameRegistry.registerTileEntity(TileAdvancedBlockExtender.class, Names.advancedBlockExtender);
        GameRegistry.registerTileEntity(TileFilteredBlockExtender.class, Names.filteredBlockExtender);
        GameRegistry.registerTileEntity(TileAdvancedFilteredBlockExtender.class, Names.advancedFilteredBlockExtender);
        GameRegistry.registerTileEntity(TileWirelessBlockExtender.class, Names.wirelessBlockExtender);
        GameRegistry.registerTileEntity(TileBuffer.class, Names.buffer);
        GameRegistry.registerTileEntity(TileAdvancedBuffer.class, Names.advancedBuffer);
        GameRegistry.registerTileEntity(TileFilteredBuffer.class, Names.filteredBuffer);
        GameRegistry.registerTileEntity(TileFilteringChest.class, Names.filteringChest);
        GameRegistry.registerTileEntity(TileFilteringConnector.class, Names.filteringConnector);

        if (Loader.isModLoaded("IronChest"))
        {
            GameRegistry.registerTileEntity(TileFilteringIronChest.class, Names.filteringIronChest);
        }

        new GuiHandler();
    }
}
