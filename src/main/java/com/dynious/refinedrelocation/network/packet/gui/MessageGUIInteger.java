package com.dynious.refinedrelocation.network.packet.gui;

import com.dynious.refinedrelocation.container.IContainerNetworked;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MessageGUIInteger extends MessageGUI implements IMessageHandler<MessageGUIInteger, IMessage>
{

    private int value;

    public MessageGUIInteger()
    {
    }

    public MessageGUIInteger(int id, int value)
    {
        super(id);
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        value = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(value);
    }

    @Override
    public IMessage onMessage(MessageGUIInteger message, MessageContext ctx)
    {
        EntityPlayer entityPlayer = ctx.side == Side.SERVER ? ctx.getServerHandler().playerEntity : FMLClientHandler.instance().getClientPlayerEntity();
        Container container = entityPlayer.openContainer;
        if (container == null || !(container instanceof IContainerNetworked))
        {
            return null;
        }

        ((IContainerNetworked) container).onMessageInteger(message.id, message.value, entityPlayer);

        return null;
    }
}
