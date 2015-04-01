package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.tileentity.IRelocator;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class MessageItemRequest implements IMessage, IMessageHandler<MessageItemRequest, MessageItemAnswer>
{
    public int x, y, z;
    public byte id;


    public MessageItemRequest()
    {
    }

    public MessageItemRequest(TileEntity tile, byte id)
    {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        id = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(id);
    }

    @Override
    public MessageItemAnswer onMessage(MessageItemRequest message, MessageContext ctx)
    {
        TileEntity tile = ctx.getServerHandler().playerEntity.getEntityWorld().getTileEntity(message.x, message.y, message.z);

        if (tile != null && tile instanceof IRelocator)
        {
            ItemStack stack = ((IRelocator) tile).getItemStackWithId(message.id);
            return new MessageItemAnswer(tile, message.id, stack);
        }
        return null;
    }
}
