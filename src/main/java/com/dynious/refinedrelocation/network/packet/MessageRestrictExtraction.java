package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerAdvancedFiltered;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MessageRestrictExtraction implements IMessage, IMessageHandler<MessageRestrictExtraction, IMessage>
{
    boolean restrictExtraction;

    public MessageRestrictExtraction()
    {
    }

    public MessageRestrictExtraction(boolean restrictExtraction)
    {
        this.restrictExtraction = restrictExtraction;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        restrictExtraction = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(restrictExtraction);
    }

    @Override
    public IMessage onMessage(MessageRestrictExtraction message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof IContainerAdvancedFiltered))
            return null;

        ((IContainerAdvancedFiltered) container).setRestrictExtraction(message.restrictExtraction);
        return null;
    }
}
