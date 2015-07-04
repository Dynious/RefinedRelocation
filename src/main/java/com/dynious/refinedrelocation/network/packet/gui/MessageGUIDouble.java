package com.dynious.refinedrelocation.network.packet.gui;

import com.dynious.refinedrelocation.container.IContainerNetworked;
import com.dynious.refinedrelocation.network.NetworkHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MessageGUIDouble extends MessageGUI implements IMessageHandler<MessageGUIDouble, IMessage>
{

    private double value;

    public MessageGUIDouble()
    {
    }

    public MessageGUIDouble(int id, double value)
    {
        super(id);
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        value = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeDouble(value);
    }

    @Override
    public IMessage onMessage(MessageGUIDouble message, MessageContext ctx)
    {
        EntityPlayer entityPlayer = ctx.side == Side.SERVER ? ctx.getServerHandler().playerEntity : NetworkHandler.getClientPlayerEntity();
        Container container = entityPlayer.openContainer;
        if (container == null || !(container instanceof IContainerNetworked))
        {
            return null;
        }

        ((IContainerNetworked) container).onMessageDouble(message.id, message.value, entityPlayer);

        return null;
    }
}
