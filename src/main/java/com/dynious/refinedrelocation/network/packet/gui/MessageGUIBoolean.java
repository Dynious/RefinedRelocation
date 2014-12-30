package com.dynious.refinedrelocation.network.packet.gui;

import com.dynious.refinedrelocation.container.ContainerRefinedRelocation;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageGUIBoolean implements IMessage, IMessageHandler<MessageGUIBoolean, IMessage>
{
    int id = 0;
    boolean bool = true;

    public MessageGUIBoolean()
    {
    }

    public MessageGUIBoolean(int id, boolean bool)
    {
        this.id = id;
        this.bool = bool;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readByte();
        bool = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(id);
        buf.writeBoolean(bool);
    }

    @Override
    public IMessage onMessage(MessageGUIBoolean message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerRefinedRelocation))
            return null;

        ((ContainerRefinedRelocation) container).onMessage(message.id, message.bool);

        return null;
    }
}
