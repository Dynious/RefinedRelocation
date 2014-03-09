package com.dynious.refinedrelocation.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class PacketTileUpdate implements IPacket
{
    public TileEntity tile;
    public NBTTagCompound compound;

    public PacketTileUpdate()
    {
    }

    public PacketTileUpdate(TileEntity tile)
    {
        this.tile = tile;
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        compound = ByteBufUtils.readTag(bytes);

        this.tile = player.getEntityWorld().getTileEntity(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
        if (tile != null)
        {
            tile.readFromNBT(compound);
        }
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        ByteBufUtils.writeTag(bytes, compound);
    }
}
