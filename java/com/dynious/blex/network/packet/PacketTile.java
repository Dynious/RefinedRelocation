package com.dynious.blex.network.packet;

import com.dynious.blex.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketTile extends CustomPacket
{
    public int x, y, z;
    public TileEntity tile;

    public PacketTile(PacketTypeHandler packetType, boolean isChunkDataPacket)
    {
        super(packetType, isChunkDataPacket);
    }

    public PacketTile(PacketTypeHandler packetType, boolean isChunkDataPacket, TileEntity tile)
    {
        super(packetType, isChunkDataPacket);
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        data.writeInt(x);
        data.writeInt(y);
        data.writeInt(z);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        x = data.readInt();
        y = data.readInt();
        z = data.readInt();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        EntityPlayer thePlayer = (EntityPlayer) player;
        System.out.println(x + ":" + y + ":" + z);
        this.tile = thePlayer.getEntityWorld().getBlockTileEntity(x, y, z);
    }
}
