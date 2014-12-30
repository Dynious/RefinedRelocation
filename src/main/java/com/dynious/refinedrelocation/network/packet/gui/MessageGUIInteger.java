package com.dynious.refinedrelocation.network.packet.gui;

import com.dynious.refinedrelocation.container.ContainerRefinedRelocation;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageGUIInteger implements IMessage, IMessageHandler<MessageGUIInteger, IMessage>
{
    int id = 0;
    int integer = 0;

    public MessageGUIInteger()
    {
    }

    public MessageGUIInteger(int id, int integer)
    {
        this.id = id;
        this.integer = integer;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readByte();
        integer = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(id);
        buf.writeInt(integer);
    }

    @Override
    public IMessage onMessage(MessageGUIInteger message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerRefinedRelocation))
            return null;

        ((ContainerRefinedRelocation) container).onMessage(message.id, message.integer);

        return null;
    }
}
