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

public class PacketMaxStackSize extends PacketTile
{
    public byte amount;

    public PacketMaxStackSize()
    {
        super(PacketTypeHandler.MAX_STACK_SIZE, false);
    }

    public PacketMaxStackSize(TileEntity tile, byte amount)
    {
        super(PacketTypeHandler.MAX_STACK_SIZE, false, tile);
        this.amount = amount;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.write(amount);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        amount = data.readByte();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);
        if (tile instanceof IAdvancedTile)
        {
            ((IAdvancedTile)tile).setMaxStackSize(amount);
        }
    }
}
