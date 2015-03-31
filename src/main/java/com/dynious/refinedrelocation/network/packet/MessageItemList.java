package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class MessageItemList implements IMessage, IMessageHandler<MessageItemList, IMessage>
{
    private int x, y, z;
    private List<TravellingItem> items;
    private List<IdAndPosition> idAndPos;

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
        idAndPos = new ArrayList<IdAndPosition>();
        int size = buf.readByte();
        for (int i = 0; i < size; i++)
        {
            byte id = buf.readByte();
            List<Byte> list = new ArrayList<Byte>();
            list.add(buf.readByte());
            byte input = buf.readByte();

            ItemStack stack;
            if (buf.readBoolean())
            {
                stack = ByteBufUtils.readItemStack(buf);
                TravellingItem item = new TravellingItem(stack, list, input);
                item.id = id;
                items.add(item);
            }
            else
            {
                idAndPos.add(new IdAndPosition(id, list, input));
            }
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
            buf.writeByte(item.id);
            buf.writeByte(item.getOutputSide());
            buf.writeByte(item.getInputSide());

            buf.writeBoolean(item.sync);
            if (item.sync)
                ByteBufUtils.writeItemStack(buf, item.getItemStack());
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
            for (IdAndPosition idAndPosition : message.idAndPos)
            {
                TileEntity te = DirectionHelper.getTileAtSide(tile, ForgeDirection.getOrientation(idAndPosition.input));
                if (te instanceof IRelocator)
                {
                    ItemStack stack = ((IRelocator) te).getItemStackWithId(idAndPosition.id);
                    if (stack != null)
                    {
                        ((IRelocator) tile).receiveTravellingItem(new TravellingItem(stack, idAndPosition.list, idAndPosition.input));
                    }
                    //TODO: Request item
                }
                //TODO: Request item
            }
        }
        return null;
    }

    private static class IdAndPosition
    {
        public byte id, input;
        public List<Byte> list;

        public IdAndPosition(byte id, List<Byte> list, byte input)
        {
            this.id = id;
            this.list = list;
            this.input = input;
        }
    }
}
