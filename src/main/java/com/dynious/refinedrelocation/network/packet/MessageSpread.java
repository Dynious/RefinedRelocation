package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerAdvanced;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MessageSpread implements IMessage, IMessageHandler<MessageSpread, IMessage>
{
    boolean spreadItems = false;

    public MessageSpread()
    {
    }

    public MessageSpread(boolean spreadItems)
    {
        this.spreadItems = spreadItems;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        spreadItems = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(spreadItems);
    }

    @Override
    public IMessage onMessage(MessageSpread message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof IContainerAdvanced))
            return null;

        ((IContainerAdvanced) container).setSpreadItems(message.spreadItems);
        return null;
    }
}
