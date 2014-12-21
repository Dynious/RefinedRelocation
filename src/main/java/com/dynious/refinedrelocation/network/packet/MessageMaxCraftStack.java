package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.container.ContainerModuleCrafting;
import com.dynious.refinedrelocation.container.IContainerAdvanced;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageMaxCraftStack implements IMessage, IMessageHandler<MessageMaxCraftStack, IMessage>
{
    public byte amount;

    public MessageMaxCraftStack()
    {
    }

    public MessageMaxCraftStack(byte amount)
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
    public IMessage onMessage(MessageMaxCraftStack message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerModuleCrafting))
            return null;

        ((ContainerModuleCrafting) container).setMaxCraftStack(message.amount);
        return null;
    }
}
