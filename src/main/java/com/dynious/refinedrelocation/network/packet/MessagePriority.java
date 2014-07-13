package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessagePriority implements IMessage, IMessageHandler<MessagePriority, IMessage>
{
    int priority;

    public MessagePriority()
    {
    }

    public MessagePriority(int priority)
    {
        this.priority = priority;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        priority = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(priority);
    }

    @Override
    public IMessage onMessage(MessagePriority message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return null;

        ((IContainerFiltered) container).setPriority(message.priority);

        return null;
    }
}
