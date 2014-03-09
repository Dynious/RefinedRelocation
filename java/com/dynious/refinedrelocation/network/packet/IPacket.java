package com.dynious.refinedrelocation.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public interface IPacket
{
    public void readBytes(ByteBuf bytes, EntityPlayer player);

    public void writeBytes(ByteBuf bytes);
}
