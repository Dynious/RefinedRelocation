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

public class MessageGUIBooleanArray extends MessageGUI implements IMessageHandler<MessageGUIBooleanArray, IMessage>
{

    private boolean[] values;

    public MessageGUIBooleanArray()
    {
    }

    public MessageGUIBooleanArray(int id, boolean[] values)
    {
        super(id);
        this.values = values;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        values = new boolean[buf.readByte()];
        for(int i = 0; i < values.length; i++) {
            values[i] = buf.readBoolean();
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeByte(values.length);
        for(int i = 0; i < values.length; i++) {
            buf.writeBoolean(values[i]);
        }
    }

    @Override
    public IMessage onMessage(MessageGUIBooleanArray message, MessageContext ctx)
    {
        EntityPlayer entityPlayer = ctx.side == Side.SERVER ? ctx.getServerHandler().playerEntity : FMLClientHandler.instance().getClientPlayerEntity();
        Container container = entityPlayer.openContainer;
        if (container == null || !(container instanceof IContainerNetworked))
        {
            return null;
        }

        ((IContainerNetworked) container).onMessageBooleanArray(message.id, message.values, entityPlayer);

        return null;
    }

}
