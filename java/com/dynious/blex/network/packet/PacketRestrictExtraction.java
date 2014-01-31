package com.dynious.blex.network.packet;

import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.tileentity.TileAdvancedFilteredBlockExtender;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketRestrictExtraction extends PacketTile
{
    public PacketRestrictExtraction()
    {
        super(PacketTypeHandler.SPREAD_ITEMS, false);
    }

    public PacketRestrictExtraction(TileEntity tile)
    {
        super(PacketTypeHandler.SPREAD_ITEMS, false, tile);
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);
        if (tile instanceof TileAdvancedFilteredBlockExtender)
        {
            ((TileAdvancedFilteredBlockExtender) tile).restrictExtraction = !((TileAdvancedFilteredBlockExtender) tile).restrictExtraction;
        }
    }
}
