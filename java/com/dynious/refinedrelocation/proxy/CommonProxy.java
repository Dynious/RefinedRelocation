package com.dynious.refinedrelocation.proxy;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.network.ChannelHandler;
import com.dynious.refinedrelocation.network.GuiHandler;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

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

        if (Loader.isModLoaded("IronChest"))
        {
            GameRegistry.registerTileEntity(TileSortingIronChest.class, Names.sortingIronChest);
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
}
