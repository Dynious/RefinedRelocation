package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.container.ContainerModuleSneaky;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageSide implements IMessage, IMessageHandler<MessageSide, IMessage>
{
    int side;

    public MessageSide()
    {
    }

    public MessageSide(int side)
    {
        this.side = side;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        side = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(side);
    }

    @Override
    public IMessage onMessage(MessageSide message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerModuleSneaky))
            return null;

        ((ContainerModuleSneaky) container).setSide(message.side);

        return null;
    }
}
