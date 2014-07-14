package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.container.ContainerPowerLimiter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageRedstoneToggle implements IMessage, IMessageHandler<MessageRedstoneToggle, IMessage>
{
    boolean toggle = true;

    public MessageRedstoneToggle()
    {
    }

    public MessageRedstoneToggle(boolean toggle)
    {
        this.toggle = toggle;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        toggle = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(toggle);
    }

    @Override
    public IMessage onMessage(MessageRedstoneToggle message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerPowerLimiter))
            return null;

        ((ContainerPowerLimiter) container).setRedstoneToggle(message.toggle);

        return null;
    }
}
