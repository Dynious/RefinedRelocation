package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MessageBlackList implements IMessage, IMessageHandler<MessageBlackList, IMessage>
{
    boolean isBlackList = true;

    public MessageBlackList()
    {
    }

    public MessageBlackList(boolean isBlackList)
    {
        this.isBlackList = isBlackList;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        isBlackList = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(isBlackList);
    }

    @Override
    public IMessage onMessage(MessageBlackList message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return null;

        ((IContainerFiltered) container).setBlackList(message.isBlackList);

        return null;
    }
}
