package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.container.ContainerModuleExtraction;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageModuleMaxStackSize implements IMessage, IMessageHandler<MessageModuleMaxStackSize, IMessage>
{
    public int maxStackSize;

    public MessageModuleMaxStackSize()
    {
    }

    public MessageModuleMaxStackSize(int state)
    {
        this.maxStackSize = state;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        maxStackSize = buf.readInt();
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(maxStackSize);
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
    public IMessage onMessage(MessageModuleMaxStackSize message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null || !(container instanceof ContainerModuleExtraction))
            return null;

        ((ContainerModuleExtraction) container).setMaxStackSize(message.maxStackSize);
        return null;
    }
}
