package com.dynious.refinedrelocation.network;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.network.packet.*;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket>
{
    public enum Packets {
        MAX_STACK_SIZE,
        INSERT_DIRECTION,
        SPREAD_ITEMS,
        USER_FILTER,
        BLACKLIST,
        FILTER_OPTION,
        TAB_SYNC,
        RESTRICT_EXTRACTION,
        REDSTONE_ENABLED,
        TILE_UPDATE,
        SWITCH_PAGE,
        SET_MAX_POWER,
        PRIORITY,
        ITEM_LIST
    }

    public ChannelHandler() {
        addDiscriminator(Packets.MAX_STACK_SIZE.ordinal(), PacketMaxStackSize.class);
        addDiscriminator(Packets.INSERT_DIRECTION.ordinal(), PacketInsertDirection.class);
        addDiscriminator(Packets.SPREAD_ITEMS.ordinal(), PacketSpread.class);
        addDiscriminator(Packets.USER_FILTER.ordinal(), PacketUserFilter.class);
        addDiscriminator(Packets.BLACKLIST.ordinal(), PacketBlacklist.class);
        addDiscriminator(Packets.FILTER_OPTION.ordinal(), PacketFilterOption.class);
        addDiscriminator(Packets.TAB_SYNC.ordinal(), PacketTabSync.class);
        addDiscriminator(Packets.RESTRICT_EXTRACTION.ordinal(), PacketRestrictExtraction.class);
        addDiscriminator(Packets.REDSTONE_ENABLED.ordinal(), PacketRedstoneEnabled.class);
        addDiscriminator(Packets.TILE_UPDATE.ordinal(), PacketTileUpdate.class);
        addDiscriminator(Packets.SWITCH_PAGE.ordinal(), PacketSwitchPage.class);
        addDiscriminator(Packets.SET_MAX_POWER.ordinal(), PacketSetMaxPower.class);
        addDiscriminator(Packets.PRIORITY.ordinal(), PacketPriority.class);
        addDiscriminator(Packets.ITEM_LIST.ordinal(), PacketItemList.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket packet, ByteBuf data) throws Exception
    {
        packet.writeBytes(data);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data, IPacket packet)
    {
        INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        EntityPlayer player = RefinedRelocation.proxy.getPlayerFromNetHandler(netHandler);
        packet.readBytes(data, player);
    }
}
