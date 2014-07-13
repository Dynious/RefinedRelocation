package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MessageUserFilter implements IMessage, IMessageHandler<MessageUserFilter, IMessage>
{
    public String userFilter;

    public MessageUserFilter()
    {
    }

    public MessageUserFilter(String userFilter)
    {
        this.userFilter = userFilter;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        userFilter = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, userFilter);
    }

    @Override
    public IMessage onMessage(MessageUserFilter message, MessageContext ctx)
    {
        Container container;
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            container = ctx.getServerHandler().playerEntity.openContainer;
        }
        else
        {
            container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;
        }

        if (!(container instanceof IContainerFiltered))
            return null;

        ((IContainerFiltered) container).setUserFilter(message.userFilter);
        return null;
    }
}
