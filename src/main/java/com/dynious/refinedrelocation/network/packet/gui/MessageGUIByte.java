package com.dynious.refinedrelocation.network.packet.gui;

import com.dynious.refinedrelocation.container.ContainerRefinedRelocation;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageGUIByte implements IMessage, IMessageHandler<MessageGUIByte, IMessage>
{
    int id = 0;
    byte byt = 0;

    public MessageGUIByte()
    {
    }

    public MessageGUIByte(int id, byte byt)
    {
        this.id = id;
        this.byt = byt;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readByte();
        byt = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(id);
        buf.writeByte(byt);
    }

    @Override
    public IMessage onMessage(MessageGUIByte message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerRefinedRelocation))
            return null;

        ((ContainerRefinedRelocation) container).onMessage(message.id, message.byt);

        return null;
    }
}
