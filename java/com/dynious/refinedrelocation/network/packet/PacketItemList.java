package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.google.common.primitives.Bytes;
import cpw.mods.fml.common.network.Player;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketItemList extends PacketTile
{
    private List<TravellingItem> items;

    public PacketItemList()
    {
        super(PacketTypeHandler.ITEM_LIST, false);
    }

    public PacketItemList(TileEntity tile, List<TravellingItem> items)
    {
        super(PacketTypeHandler.ITEM_LIST, false, tile);
        this.items = items;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeByte(items.size());
        for (TravellingItem item : items)
        {
            Packet.writeItemStack(item.getItemStack(), data);
            data.writeByte(item.getPath().size());
            data.write(Bytes.toArray(item.getPath()));
            data.writeByte(item.input);
        }
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        items = new ArrayList<TravellingItem>();
        int size = data.readByte();
        for (int i = 0; i < size; i++)
        {
            ItemStack stack = Packet.readItemStack(data);
            byte[] path = new byte[data.readByte()];
            data.read(path);
            byte input = data.readByte();
            items.add(new TravellingItem(stack, (IRelocator) tile, Bytes.asList(path), input));
        }
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);
        for (TravellingItem item : items)
        {
            ((IRelocator) tile).receiveTravellingItem(item);
        }
    }
}
