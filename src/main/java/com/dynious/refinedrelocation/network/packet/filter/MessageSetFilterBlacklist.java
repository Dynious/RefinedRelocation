package com.dynious.refinedrelocation.network.packet.filter;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.container.IContainerFiltered;
import com.dynious.refinedrelocation.network.NetworkHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageSetFilterBlacklist implements IMessage, IMessageHandler<MessageSetFilterBlacklist, IMessage>
{
    private int filterIndex;
    private boolean blacklist;

    public MessageSetFilterBlacklist()
    {
    }

    public MessageSetFilterBlacklist(int filterIndex, boolean blacklist)
    {
        this.filterIndex = filterIndex;
        this.blacklist = blacklist;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        filterIndex = buf.readByte();
        blacklist = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(filterIndex);
        buf.writeBoolean(blacklist);
    }

    @Override
    public IMessage onMessage(MessageSetFilterBlacklist message, MessageContext ctx)
    {
        Container container = null;
        if (ctx.side == Side.CLIENT)
        {
            container = NetworkHandler.getClientPlayerEntity().openContainer;
        } else if (ctx.side == Side.SERVER)
        {
            container = ctx.getServerHandler().playerEntity.openContainer;
        }
        if (container == null || !(container instanceof IContainerFiltered))
        {
            return null;
        }
        IFilterGUI filter = ((IContainerFiltered) container).getFilter();
        if(message.filterIndex >= 0 && message.filterIndex < filter.getFilterCount()) {
            filter.getFilterAtIndex(message.filterIndex).setBlacklist(message.blacklist);
        }
        return null;
    }
}
