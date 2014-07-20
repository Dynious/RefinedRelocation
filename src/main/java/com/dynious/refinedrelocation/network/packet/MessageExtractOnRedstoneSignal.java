package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.container.ContainerModuleExtraction;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageExtractOnRedstoneSignal implements IMessage, IMessageHandler<MessageExtractOnRedstoneSignal, IMessage>
{
    public int state;

    public MessageExtractOnRedstoneSignal()
    {
    }

    public MessageExtractOnRedstoneSignal(int state)
    {
        this.state = state;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        state = buf.readInt();
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(state);
    }

    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(MessageExtractOnRedstoneSignal message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerModuleExtraction))
            return null;

        ((ContainerModuleExtraction) container).setRedstoneControlState(message.state);
        return null;
    }
}
