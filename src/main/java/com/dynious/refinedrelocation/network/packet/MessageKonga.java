package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.client.KongaHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageKonga implements IMessage, IMessageHandler<MessageKonga, IMessage>
{

    public MessageKonga()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    }

    @Override
    public IMessage onMessage(MessageKonga message, MessageContext ctx)
    {
        KongaHandler.toggleKonga();

        return null;
    }
}
