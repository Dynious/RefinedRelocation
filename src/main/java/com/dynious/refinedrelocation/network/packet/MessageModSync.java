package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.grid.filter.ModFilter;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageModSync implements IMessage, IMessageHandler<MessageModSync, IMessage>
{
    public String[] modIDs;

    public MessageModSync()
    {
    }

    public MessageModSync(String[] modIDs)
    {
        this.modIDs = modIDs;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        modIDs = new String[buf.readInt()];
        for (int i = 0; i < modIDs.length; i++)
        {
            modIDs[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(modIDs.length);
        for (String label : modIDs)
        {
            ByteBufUtils.writeUTF8String(buf, label);
        }
    }

    @Override
    public IMessage onMessage(MessageModSync message, MessageContext ctx)
    {
        ModFilter.syncMods(message.modIDs);
        return null;
    }
}
