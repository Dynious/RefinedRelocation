package com.dynious.refinedrelocation.network.packet.filter;

import com.dynious.refinedrelocation.container.IContainerFiltered;
import com.dynious.refinedrelocation.grid.filter.AbstractFilter;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageSetFilterBooleanArray implements IMessage, IMessageHandler<MessageSetFilterBooleanArray, IMessage> {
    public int filterIndex;
    public int filterOption;
    public boolean[] filterStates;

    public MessageSetFilterBooleanArray() {}

    public MessageSetFilterBooleanArray(int filterIndex, int filterOption, boolean[] filterStates) {
        this.filterIndex = filterIndex;
        this.filterOption = filterOption;
        this.filterStates = filterStates;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        filterIndex = buf.readByte();
        filterOption = buf.readInt();
        filterStates = new boolean[buf.readByte()];
        for(int i = 0; i < filterStates.length; i++) {
            filterStates[i] = buf.readBoolean();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(filterIndex);
        buf.writeInt(filterOption);
        buf.writeByte(filterStates.length);
        for(int i = 0; i < filterStates.length; i++) {
            buf.writeBoolean(filterStates[i]);
        }
    }

    @Override
    public IMessage onMessage(MessageSetFilterBooleanArray message, MessageContext ctx) {
        Container container = null;
        if(ctx.side == Side.CLIENT) {
            container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;
        } else if(ctx.side == Side.SERVER) {
            container = ctx.getServerHandler().playerEntity.openContainer;
        }
        if (container == null || !(container instanceof IContainerFiltered)) {
            return null;
        }
        AbstractFilter filter = ((IContainerFiltered) container).getFilter().getFilterAtIndex(message.filterIndex);
        filter.setFilterBooleanArray(message.filterOption, message.filterStates);
        return null;
    }
}
