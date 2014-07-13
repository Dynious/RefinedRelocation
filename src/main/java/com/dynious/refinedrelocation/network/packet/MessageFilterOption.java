package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MessageFilterOption implements IMessage, IMessageHandler<MessageFilterOption, IMessage>
{
    public int filterIndex;

    public MessageFilterOption()
    {
    }

    public MessageFilterOption(int filterIndex)
    {
        this.filterIndex = filterIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        filterIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(filterIndex);
    }

    @Override
    public IMessage onMessage(MessageFilterOption message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return null;

        ((IContainerFiltered) container).toggleFilterOption(message.filterIndex);

        return null;
    }
}
