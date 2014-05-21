package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketPriority implements IPacket
{
    int priority;

    public PacketPriority()
    {
    }

    public PacketPriority(int priority)
    {
        this.priority = priority;
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
    bytes.writeByte(priority);
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        priority = bytes.readByte();

        Container container = player.openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return;

        ((IContainerFiltered) container).setPriority(priority);
    }
}
