package com.dynious.refinedrelocation.network.packet.filter;

import com.dynious.refinedrelocation.container.IContainerFiltered;
import com.dynious.refinedrelocation.grid.filter.AbstractFilter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class MessageSetFilterBoolean implements IMessage, IMessageHandler<MessageSetFilterBoolean, IMessage> {
    public int filterIndex;
    public int filterOption;
    public boolean filterState;

    public MessageSetFilterBoolean() {}

    public MessageSetFilterBoolean(int filterIndex, int filterOption, boolean filterState) {
        this.filterIndex = filterIndex;
        this.filterOption = filterOption;
        this.filterState = filterState;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        filterIndex = buf.readByte();
        filterOption = buf.readInt();
        filterState = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(filterIndex);
        buf.writeInt(filterOption);
        buf.writeBoolean(filterState);
    }

    @Override
    public IMessage onMessage(MessageSetFilterBoolean message, MessageContext ctx) {
        Container container = Minecraft.getMinecraft().thePlayer.openContainer;
        if (container == null || !(container instanceof IContainerFiltered)) {
            return null;
        }
        AbstractFilter filter = ((IContainerFiltered) container).getFilter().getFilterAtIndex(filterIndex);
        filter.setFilterBoolean(message.filterOption, message.filterState);
        return null;
    }
}
