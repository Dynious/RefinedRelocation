package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerAdvanced;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketMaxStackSize implements IPacket
{
    public byte amount;

    public PacketMaxStackSize()
    {
    }

    public PacketMaxStackSize(byte amount)
    {
        this.amount = amount;
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        amount = bytes.readByte();

        Container container = player.openContainer;

        if (container == null || !(container instanceof IContainerAdvanced))
            return;

        ((IContainerAdvanced) container).setMaxStackSize(amount);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeByte(amount);
    }
}
