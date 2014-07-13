package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerAdvanced;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageInsertDirection implements IMessage, IMessageHandler<MessageInsertDirection, IMessage>
{
    public byte sideAndDirection;

    public MessageInsertDirection()
    {
    }

    public MessageInsertDirection(byte side, byte direction)
    {
        this.sideAndDirection = (byte) ((byte) (side << 4) | (byte) (direction));
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        sideAndDirection = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(sideAndDirection);
    }

    @Override
    public IMessage onMessage(MessageInsertDirection message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof IContainerAdvanced))
            return null;

        ((IContainerAdvanced) container).setInsertDirection(message.sideAndDirection >> 4, message.sideAndDirection & 15); // 15 = 0b1111

        return null;
    }
}
