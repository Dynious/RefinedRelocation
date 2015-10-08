package com.dynious.refinedrelocation;

import com.dynious.refinedrelocation.event.InitialSyncHandler;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.network.GuiHandler;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

import java.util.EnumMap;

public class CommonProxy {
    protected EnumMap<Side, FMLEmbeddedChannel> channels;

    public void initTileEntities() {
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
        GameRegistry.registerTileEntity(TilePlayerRelocatorBase.class, Names.playerRelocatorBase);
        GameRegistry.registerTileEntity(TilePowerLimiter.class, Names.powerLimiter);
        GameRegistry.registerTileEntity(TileSortingInterface.class, Names.sortingInterface);
        GameRegistry.registerTileEntity(TileSortingInputPane.class, Names.sortingInputPane);
        GameRegistry.registerTileEntity(TileRelocator.class, Names.relocator);

        if (Mods.IS_IRON_CHEST_LOADED) {
            GameRegistry.registerTileEntity(TileSortingIronChest.class, Names.sortingIronChest);
        }

        if (Mods.IS_JABBA_LOADED) {
            GameRegistry.registerTileEntity(TileSortingBarrel.class, Names.sortingBarrel);
        }

        if (Mods.IS_EE3_LOADED) {
            GameRegistry.registerTileEntity(TileSortingAlchemicalChest.class, Names.sortingAlchemicalChest);
        }

        if (Mods.IS_AE2_LOADED) {
            GameRegistry.registerTileEntity(TileMESortingInterface.class, Names.MESortingInterface);
        }

        new GuiHandler();
    }

    public void registerEventHandlers() {
        InitialSyncHandler ev = new InitialSyncHandler();
        FMLCommonHandler.instance().bus().register(ev);
        MinecraftForge.EVENT_BUS.register(ev);
    }

    public void init(FMLInitializationEvent event) {}
}
