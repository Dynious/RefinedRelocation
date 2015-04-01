package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class MessageItemAnswer implements IMessage, IMessageHandler<MessageItemAnswer, IMessage>
{
    public int x, y, z;
    public byte id;
    public ItemStack stack;

    public MessageItemAnswer()
    {
    }

    public MessageItemAnswer(TileEntity tile, byte id, ItemStack stack)
    {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
        this.id = id;
        this.stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        id = buf.readByte();
        stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(id);
        ByteBufUtils.writeItemStack(buf, stack);
    }

    @Override
    public IMessage onMessage(MessageItemAnswer message, MessageContext ctx)
    {
        TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);

        if (tile != null && tile instanceof IRelocator)
        {
            for (TravellingItem item : ((IRelocator) tile).getItems(false))
            {
                if (item.id == message.id)
                {
                    item.itemStack = message.stack;
                    break;
                }
            }
        }
        return null;
    }
}
