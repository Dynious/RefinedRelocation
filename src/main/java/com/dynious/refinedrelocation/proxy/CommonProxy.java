package com.dynious.refinedrelocation.proxy;

import com.dynious.refinedrelocation.event.EventHandler;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.network.ChannelHandler;
import com.dynious.refinedrelocation.network.GuiHandler;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.MinecraftForge;

import java.util.EnumMap;

public class CommonProxy
{
    protected EnumMap<Side,FMLEmbeddedChannel> channels;

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

        if (Mods.IS_IRON_CHEST_LOADED)
        {
            GameRegistry.registerTileEntity(TileSortingIronChest.class, Names.sortingIronChest);
        }
        if (Mods.IS_JABBA_LOADED)
        {
            GameRegistry.registerTileEntity(TileSortingBarrel.class, Names.sortingBarrel);
        }

        if (Mods.IS_EE3_LOADED)
        {
            GameRegistry.registerTileEntity(TileSortingAlchemicalChest.class, Names.sortingAlchemicalChest);
        }

        new GuiHandler();
    }

    public void initNetworking()
    {
        channels = NetworkRegistry.INSTANCE.newChannel(Reference.MOD_ID, new ChannelHandler());
    }

    public EmbeddedChannel getChannel()
    {
        return channels.get(Side.SERVER);
    }

    public EntityPlayer getPlayerFromNetHandler (INetHandler handler)
    {
        if (handler instanceof NetHandlerPlayServer)
        {
            return ((NetHandlerPlayServer) handler).playerEntity;
        }
        else
        {
            return null;
        }
    }

    public void registerEventHandlers()
    {
        EventHandler ev = new EventHandler();
        FMLCommonHandler.instance().bus().register(ev);
        MinecraftForge.EVENT_BUS.register(ev);
    }
}
