package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class MessageItemList implements IMessage, IMessageHandler<MessageItemList, IMessage>
{
    private int x, y, z;
    private List<TravellingItem> items;

    public MessageItemList()
    {
    }

    public MessageItemList(TileEntity tile, List<TravellingItem> items)
    {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
        this.items = items;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();

        items = new ArrayList<TravellingItem>();
        int size = buf.readByte();
        for (int i = 0; i < size; i++)
        {
            ItemStack stack = ByteBufUtils.readItemStack(buf);
            List<Byte> list = new ArrayList<Byte>();
            //byte[] path = new byte[data.readByte()];
            //data.read(path);
            list.add(buf.readByte());
            byte input = buf.readByte();
            items.add(new TravellingItem(stack, list, input));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);

        buf.writeByte(items.size());
        for (TravellingItem item : items)
        {
            ByteBufUtils.writeItemStack(buf, item.getItemStack());
            buf.writeByte(item.getOutputSide());
            //data.writeByte(item.getPath().size());
            //data.write(Bytes.toArray(item.getPath()));
            buf.writeByte(item.getInputSide());
        }
    }

    @Override
    public IMessage onMessage(MessageItemList message, MessageContext ctx)
    {
        TileEntity tile = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);

        if (tile != null)
        {
            for (TravellingItem item : message.items)
            {
                ((IRelocator) tile).receiveTravellingItem(item);
            }
        }
        return null;
    }
}
