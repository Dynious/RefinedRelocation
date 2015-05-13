package com.dynious.refinedrelocation.network.packet.gui;

import com.dynious.refinedrelocation.container.ContainerRefinedRelocation;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MessageGUIByte extends MessageGUI implements IMessageHandler<MessageGUIByte, IMessage> {

    private byte value;

    public MessageGUIByte() {
    }

    public MessageGUIByte(int id, byte value) {
        super(id);
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        value = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeByte(value);
    }

    @Override
    public IMessage onMessage(MessageGUIByte message, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
        Container container = entityPlayer.openContainer;
        if(container == null || !(container instanceof ContainerRefinedRelocation)) {
            return null;
        }

        ((ContainerRefinedRelocation) container).onMessage(message.id, message.value, entityPlayer);

        return null;
    }
}
