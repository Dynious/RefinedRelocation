package com.dynious.refinedrelocation.proxy;

import com.dynious.refinedrelocation.event.EventHandler;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.network.GuiHandler;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;

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
        GameRegistry.registerTileEntity(TileSortingChest.class, Names.sortingChest);
        GameRegistry.registerTileEntity(TileSortingConnector.class, Names.sortingConnector);
        GameRegistry.registerTileEntity(TileFilteringHopper.class, Names.filteringHopper);
        GameRegistry.registerTileEntity(TileRelocationPortal.class, Names.relocationPortal);
        GameRegistry.registerTileEntity(TileRelocationController.class, Names.relocationController);
        GameRegistry.registerTileEntity(TilePowerLimiter.class, Names.powerLimiter);
        GameRegistry.registerTileEntity(TileSortingInterface.class, Names.sortingInterface);
        GameRegistry.registerTileEntity(TileSortingImporter.class, Names.sortingImporter);
        GameRegistry.registerTileEntity(TileRelocator.class, Names.relocator);

        if (Loader.isModLoaded("IronChest"))
        {
            GameRegistry.registerTileEntity(TileSortingIronChest.class, Names.sortingIronChest);
        }
        if (Loader.isModLoaded("JABBA"))
        {
            GameRegistry.registerTileEntity(TileSortingBarrel.class, Names.sortingBarrel);
        }

        if (Loader.isModLoaded("EE3"))
        {
            GameRegistry.registerTileEntity(TileSortingAlchemicalChest.class, Names.sortingAlchemicalChest);
        }

        new GuiHandler();
    }

    public void registerEventHandlers()
    {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}
