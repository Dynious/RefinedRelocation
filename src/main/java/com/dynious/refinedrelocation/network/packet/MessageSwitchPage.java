package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.container.ContainerSortingImporter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageSwitchPage implements IMessage, IMessageHandler<MessageSwitchPage, IMessage>
{
    private boolean previous;

    public MessageSwitchPage()
    {
    }

    public MessageSwitchPage(boolean previous)
    {
        this.previous = previous;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        previous = buf.readBoolean();
    }


    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(previous);
    }

    @Override
    public IMessage onMessage(MessageSwitchPage message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerSortingImporter))
            return null;

        if (previous)
        {
            ((ContainerSortingImporter) container).previousPage();
        }
        else
        {
            ((ContainerSortingImporter) container).nextPage();
        }
        return null;
    }
}
