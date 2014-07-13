package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerModuleSneaky;
import com.dynious.refinedrelocation.gui.container.ContainerModuleSneakyExtraction;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageExtractionSide implements IMessage, IMessageHandler<MessageExtractionSide, IMessage>
{
    int side;

    public MessageExtractionSide()
    {
    }

    public MessageExtractionSide(int side)
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
    public IMessage onMessage(MessageExtractionSide message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerModuleSneakyExtraction))
            return null;

        ((ContainerModuleSneakyExtraction) container).setExtractionSide(side);

        return null;
    }
}
