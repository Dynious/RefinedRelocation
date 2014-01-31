package com.dynious.blex.network.packet;

import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.tileentity.IFilterTile;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketFilterOption extends PacketTile
{
    public byte from;

    public PacketFilterOption()
    {
        super(PacketTypeHandler.FILTER_OPTION, false);
    }

    public PacketFilterOption(TileEntity tile, byte from)
    {
        super(PacketTypeHandler.FILTER_OPTION, false, tile);
        this.from = from;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.write(from);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        from = data.readByte();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);
        if (tile instanceof IFilterTile)
        {
            ((IFilterTile) tile).getFilter().setValue(from, !((IFilterTile) tile).getFilter().getValue(from));
        }
    }
}
