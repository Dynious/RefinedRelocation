package com.dynious.refinedrelocation.network;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.network.packet.IPacket;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.entity.player.EntityPlayer;

public class NetworkHelper
{
    /**
     * Send this message to everyone.
     * SERVER
     *
     * @param message The message to send
     */
    public static void sendToAll(IPacket message)
    {
        RefinedRelocation.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        RefinedRelocation.proxy.getChannel().writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this message to the specified player.
     * SERVER
     *
     * @param message The message to send
     * @param player The player to send it to
     */
    public static void sendTo(IPacket message, EntityPlayer player)
    {
        RefinedRelocation.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        RefinedRelocation.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        RefinedRelocation.proxy.getChannel().writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * SERVER
     *
     * @param message The message to send
     * @param point The {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint} around which to send
     */
    public static void sendToAllAround(IPacket message, NetworkRegistry.TargetPoint point)
    {
        RefinedRelocation.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        RefinedRelocation.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        RefinedRelocation.proxy.getChannel().writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * SERVER
     *
     * @param message The message to send
     * @param dimensionId The dimension id to target
     */
    public static void sendToDimension(IPacket message, int dimensionId)
    {
        RefinedRelocation.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        RefinedRelocation.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        RefinedRelocation.proxy.getChannel().writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this message to the server.
     * CLIENT
     *
     * @param message The message to send
     */
    public static void sendToServer(IPacket message)
    {
        RefinedRelocation.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        RefinedRelocation.proxy.getChannel().writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}
