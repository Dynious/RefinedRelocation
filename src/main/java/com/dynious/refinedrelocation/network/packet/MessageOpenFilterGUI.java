package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.container.IContainerAdvanced;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class MessageOpenFilterGUI implements IMessage, IMessageHandler<MessageOpenFilterGUI, IMessage>
{
    private int x;
    private int y;
    private int z;

    public MessageOpenFilterGUI()
    {
    }

    public MessageOpenFilterGUI(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public IMessage onMessage(MessageOpenFilterGUI message, MessageContext ctx)
    {
        APIUtils.openFilteringGUI(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, message.x, message.y, message.z);
        return null;
    }
}
