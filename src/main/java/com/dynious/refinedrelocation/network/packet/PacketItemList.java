package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class PacketItemList extends PacketTile
{
    private List<TravellingItem> items;

    public PacketItemList()
    {
    }

    public PacketItemList(TileEntity tile, List<TravellingItem> items)
    {
        this.items = items;
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        super.writeBytes(bytes);
        bytes.writeByte(items.size());
        for (TravellingItem item : items)
        {
            ByteBufUtils.writeItemStack(bytes, item.getItemStack());
            bytes.writeByte(item.getPath().get(0));
            //data.writeByte(item.getPath().size());
            //data.write(Bytes.toArray(item.getPath()));
            bytes.writeByte(item.input);
        }
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        super.readBytes(bytes, player);
        items = new ArrayList<TravellingItem>();
        int size = bytes.readByte();
        for (int i = 0; i < size; i++)
        {
            ItemStack stack = ByteBufUtils.readItemStack(bytes);
            List<Byte> list = new ArrayList<Byte>();
            //byte[] path = new byte[data.readByte()];
            //data.read(path);
            list.add(bytes.readByte());
            byte input = bytes.readByte();
            items.add(new TravellingItem(stack, null, list, input));
        }

        if (tile != null)
        {
            for (TravellingItem item : items)
            {
                item.counter--;
                ((IRelocator) tile).receiveTravellingItem(item);
            }
        }
    }
}
