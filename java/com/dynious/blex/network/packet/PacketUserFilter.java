package com.dynious.blex.network.packet;

import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.tileentity.IFilterTile;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketUserFilter extends PacketTile
{
    public String userFilter;

    public PacketUserFilter()
    {
        super(PacketTypeHandler.USER_FILTER, false);
    }

    public PacketUserFilter(TileEntity tile, String userFilter)
    {
        super(PacketTypeHandler.USER_FILTER, false, tile);
        this.userFilter = userFilter;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeUTF(userFilter);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        userFilter = data.readUTF();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);
        if (tile instanceof IFilterTile)
        {
            ((IFilterTile) tile).getFilter().userFilter = userFilter;
        }
    }
}
