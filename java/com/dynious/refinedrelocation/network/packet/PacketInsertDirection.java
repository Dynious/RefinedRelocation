package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerAdvanced;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketInsertDirection implements IPacket
{
    public byte sideAndDirection;

    public PacketInsertDirection()
    {
    }

    public PacketInsertDirection(byte side, byte direction)
    {
        this.sideAndDirection = (byte) ((byte) (side << 4) | (byte) (direction));
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        sideAndDirection = bytes.readByte();

        Container container = player.openContainer;

        if (container == null || !(container instanceof IContainerAdvanced))
            return;

        ((IContainerAdvanced) container).setInsertDirection(sideAndDirection >> 4, sideAndDirection & 15); // 15 = 0b1111
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeByte(sideAndDirection);
    }
}
