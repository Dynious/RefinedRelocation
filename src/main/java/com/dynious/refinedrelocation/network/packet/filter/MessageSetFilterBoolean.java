package com.dynious.refinedrelocation.network.packet.filter;

import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.container.IContainerFiltered;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
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
        Container container = null;
        if(ctx.side == Side.CLIENT) {
            container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;
        } else if(ctx.side == Side.SERVER) {
            container = ctx.getServerHandler().playerEntity.openContainer;
        }
        if (container == null || !(container instanceof IContainerFiltered)) {
            return null;
        }
        IMultiFilterChild filter = ((IContainerFiltered) container).getFilter().getFilterAtIndex(message.filterIndex);
        filter.setFilterBoolean(message.filterOption, message.filterState);
        return null;
    }
}
