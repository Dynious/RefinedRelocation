package com.dynious.refinedrelocation.network.packet.filter;

import com.dynious.refinedrelocation.container.IContainerFiltered;
import com.dynious.refinedrelocation.grid.filter.AbstractFilter;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class MessageSetFilterString implements IMessage, IMessageHandler<MessageSetFilterString, IMessage> {

    public int filterIndex;
    public int filterOption;
    public String filterString;

    public MessageSetFilterString() {}

    public MessageSetFilterString(int filterIndex, int filterOption, String filterString) {
        this.filterIndex = filterIndex;
        this.filterOption = filterOption;
        this.filterString = filterString;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        filterIndex = buf.readByte();
        filterOption = buf.readInt();
        filterString = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(filterIndex);
        buf.writeInt(filterOption);
        ByteBufUtils.writeUTF8String(buf, filterString);
    }

    @Override
    public IMessage onMessage(MessageSetFilterString message, MessageContext ctx) {
        Container container = Minecraft.getMinecraft().thePlayer.openContainer;
        if (container == null || !(container instanceof IContainerFiltered)) {
            return null;
        }
        AbstractFilter filter = ((IContainerFiltered) container).getFilter().getFilterAtIndex(filterIndex);
        filter.setFilterString(message.filterOption, message.filterString);
        return null;
    }
}
