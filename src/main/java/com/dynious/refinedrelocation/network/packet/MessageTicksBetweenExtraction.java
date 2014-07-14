package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.container.ContainerModuleExtraction;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageTicksBetweenExtraction implements IMessage, IMessageHandler<MessageTicksBetweenExtraction, IMessage>
{
    public int amount;

    public MessageTicksBetweenExtraction()
    {
    }

    public MessageTicksBetweenExtraction(int amount)
    {
        this.amount = amount;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        amount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(amount);
    }

    @Override
    public IMessage onMessage(MessageTicksBetweenExtraction message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerModuleExtraction))
            return null;

        ((ContainerModuleExtraction) container).setTicksBetweenExtraction(message.amount);

        return null;
    }
}
