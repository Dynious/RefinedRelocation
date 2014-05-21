package com.dynious.refinedrelocation.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PacketTile implements IPacket
{
    public int x, y, z;
    public TileEntity tile;

    public PacketTile()
    {
    }

    public PacketTile(TileEntity tile)
    {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        x = bytes.readInt();
        y = bytes.readInt();
        z = bytes.readInt();

        this.tile = player.getEntityWorld().getTileEntity(x, y, z);
    }
}
