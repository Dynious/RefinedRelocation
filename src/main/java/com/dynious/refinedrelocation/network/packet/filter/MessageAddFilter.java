package com.dynious.refinedrelocation.network.packet.filter;

import com.dynious.refinedrelocation.container.IContainerFiltered;
import com.dynious.refinedrelocation.grid.filter.AbstractFilter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class MessageAddFilter implements IMessage, IMessageHandler<MessageAddFilter, IMessage> {
    private int filterType;

    public MessageAddFilter() {}

    public MessageAddFilter(int filterType) {
        this.filterType = filterType;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        filterType = buf.readInt();
        System.out.println(filterType);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(filterType);
    }

    @Override
    public IMessage onMessage(MessageAddFilter message, MessageContext ctx) {
        Container container = Minecraft.getMinecraft().thePlayer.openContainer;
        if (container == null || !(container instanceof IContainerFiltered)) {
            return null;
        }
        ((IContainerFiltered) container).getFilter().addNewFilter(message.filterType);
        return null;
    }
}
