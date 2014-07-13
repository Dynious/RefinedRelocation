package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerMultiModule;
import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageHomeButtonClicked implements IMessage, IMessageHandler<MessageHomeButtonClicked, IMessage>
{
    int index;

    public MessageHomeButtonClicked()
    {
    }

    public MessageHomeButtonClicked(int index)
    {
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        index = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(index);
    }

    @Override
    public IMessage onMessage(MessageHomeButtonClicked message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerMultiModule))
            return null;

        ((ContainerMultiModule) container).openOrActive(message.index);

        return null;
    }
}
