package com.dynious.refinedrelocation.proxy;

import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.network.GuiHandler;
import com.dynious.refinedrelocation.tileentity.*;
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
        GameRegistry.registerTileEntity(TileFilteringHopper.class, Names.filteringHopper);

        if (Loader.isModLoaded("IronChest"))
        {
            GameRegistry.registerTileEntity(TileFilteringIronChest.class, Names.filteringIronChest);
        }

        new GuiHandler();
    }
}
