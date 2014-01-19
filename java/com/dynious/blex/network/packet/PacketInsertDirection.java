package com.dynious.blex.network.packet;

import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.tileentity.IAdvancedTile;
import com.dynious.blex.tileentity.TileAdvancedBlockExtender;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketInsertDirection extends PacketTile
{
    public byte from;

    public PacketInsertDirection()
    {
        super(PacketTypeHandler.INSERT_DIRECTION, false);
    }

    public PacketInsertDirection(TileEntity tile, byte from)
    {
        super(PacketTypeHandler.INSERT_DIRECTION, false, tile);
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
        if (tile instanceof IAdvancedTile)
        {
            ((IAdvancedTile)tile).setInsertDirection(from, ((IAdvancedTile)tile).getInsertDirection()[from] + 1);
        }
    }
}
