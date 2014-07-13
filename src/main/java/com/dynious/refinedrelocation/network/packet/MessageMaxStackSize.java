package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerAdvanced;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageMaxStackSize implements IMessage, IMessageHandler<MessageMaxStackSize, IMessage>
{
    public byte amount;

    public MessageMaxStackSize()
    {
    }

    public MessageMaxStackSize(byte amount)
    {
        this.amount = amount;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        amount = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(amount);
    }

    @Override
    public IMessage onMessage(MessageMaxStackSize message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof IContainerAdvanced))
            return null;

        ((IContainerAdvanced) container).setMaxStackSize(message.amount);
        return null;
    }
}
