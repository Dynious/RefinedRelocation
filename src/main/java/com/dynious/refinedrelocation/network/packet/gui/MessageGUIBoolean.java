package com.dynious.refinedrelocation.network.packet.gui;

import com.dynious.refinedrelocation.container.ContainerRefinedRelocation;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MessageGUIBoolean extends MessageGUI implements IMessageHandler<MessageGUIBoolean, IMessage>  {

    private boolean value = true;

    public MessageGUIBoolean() {}

    public MessageGUIBoolean(int id, boolean value) {
        super(id);
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        value = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(value);
    }

    @Override
    public IMessage onMessage(MessageGUIBoolean message, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
        Container container = entityPlayer.openContainer;
        if(container == null || !(container instanceof ContainerRefinedRelocation)) {
            return null;
        }

        ((ContainerRefinedRelocation) container).onMessage(message.id, message.value, entityPlayer);

        return null;
    }

}
