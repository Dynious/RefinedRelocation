package com.dynious.refinedrelocation.network.packet.gui;

import com.dynious.refinedrelocation.container.ContainerRefinedRelocation;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageGUIByteClient implements IMessage, IMessageHandler<MessageGUIByteClient, IMessage>
{
    int id = 0;
    byte byt = 0;

    public MessageGUIByteClient()
    {
    }

    public MessageGUIByteClient(int id, byte byt)
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
    public IMessage onMessage(MessageGUIByteClient message, MessageContext ctx)
    {
        Container container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;

        if (container == null || !(container instanceof ContainerRefinedRelocation))
            return null;

        ((ContainerRefinedRelocation) container).onSyncMessage(message.id, message.byt);

        return null;
    }
}
