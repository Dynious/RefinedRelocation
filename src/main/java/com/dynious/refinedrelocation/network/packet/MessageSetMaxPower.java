package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerPowerLimiter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageSetMaxPower implements IMessage, IMessageHandler<MessageSetMaxPower, IMessage>
{
    public double amount;

    public MessageSetMaxPower()
    {
    }

    public MessageSetMaxPower(double amount)
    {
        this.amount = amount;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        amount = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeDouble(amount);
    }

    @Override
    public IMessage onMessage(MessageSetMaxPower message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerPowerLimiter))
            return null;

        ((ContainerPowerLimiter) container).setMaxAcceptedEnergy(message.amount);
        return null;
    }
}
