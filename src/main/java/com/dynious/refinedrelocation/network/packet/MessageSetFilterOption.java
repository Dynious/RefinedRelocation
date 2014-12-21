package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.container.IContainerFiltered;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class MessageSetFilterOption implements IMessage, IMessageHandler<MessageSetFilterOption, IMessage>
{
    public int filterIndex;
    public boolean filterState;

    public MessageSetFilterOption()
    {
    }

    public MessageSetFilterOption(int filterIndex, boolean filterState)
    {
        this.filterIndex = filterIndex;
        this.filterState = filterState;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        filterIndex = buf.readInt();
        filterState = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(filterIndex);
        buf.writeBoolean(filterState);
    }

    @Override
    public IMessage onMessage(MessageSetFilterOption message, MessageContext ctx)
    {
        Container container = Minecraft.getMinecraft().thePlayer.openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return null;

        ((IContainerFiltered) container).setFilterOption(message.filterIndex, message.filterState);

        return null;
    }
}
