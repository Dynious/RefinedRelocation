package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketFilterOption implements IPacket
{
    public byte filterIndex;

    public PacketFilterOption()
    {
    }

    public PacketFilterOption(byte filterIndex)
    {
        this.filterIndex = filterIndex;
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        filterIndex = bytes.readByte();

        Container container = player.openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return;

        ((IContainerFiltered) container).toggleFilterOption(filterIndex);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeByte(filterIndex);
    }
}
