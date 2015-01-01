package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.grid.FilterCreativeTabs;
import com.dynious.refinedrelocation.grid.FilterStandard;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageTabSync implements IMessage, IMessageHandler<MessageTabSync, IMessage>
{
    public String[] labels;

    public MessageTabSync()
    {
    }

    public MessageTabSync(String[] labels)
    {
        this.labels = labels;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        labels = new String[buf.readInt()];
        for (int i = 0; i < labels.length; i++)
        {
            labels[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(labels.length);
        for (String label : labels)
        {
            ByteBufUtils.writeUTF8String(buf, label);
        }
    }

    @Override
    public IMessage onMessage(MessageTabSync message, MessageContext ctx)
    {
        FilterStandard.syncTabs(message.labels);
        FilterCreativeTabs.syncTabs(message.labels);
        return null;
    }
}
