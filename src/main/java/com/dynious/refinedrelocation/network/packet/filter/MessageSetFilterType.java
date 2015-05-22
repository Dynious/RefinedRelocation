package com.dynious.refinedrelocation.network.packet.filter;

import com.dynious.refinedrelocation.container.IContainerFiltered;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class MessageSetFilterType implements IMessage, IMessageHandler<MessageSetFilterType, IMessage> {
    private int filterIndex;
    private int filterType;

    public MessageSetFilterType() {}

    public MessageSetFilterType(int filterIndex, int filterType) {
        this.filterIndex = filterIndex;
        this.filterType = filterType;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        filterIndex = buf.readByte();
        filterType = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(filterIndex);
        buf.writeByte(filterType);
    }

    @Override
    public IMessage onMessage(MessageSetFilterType message, MessageContext ctx) {
        Container container = null;
        if(ctx.side == Side.CLIENT) {
            container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;
        } else if(ctx.side == Side.SERVER) {
            container = ctx.getServerHandler().playerEntity.openContainer;
        }
        if (container == null || !(container instanceof IContainerFiltered)) {
            return null;
        }
        ((IContainerFiltered) container).getFilter().setFilterType(message.filterIndex, message.filterType);
        return null;
    }
}
