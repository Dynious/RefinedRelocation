package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketTileUpdate extends CustomPacket
{
    public TileEntity tile;
    public NBTTagCompound compound;

    public PacketTileUpdate()
    {
        super(PacketTypeHandler.TILE_UPDATE, false);
    }

    public PacketTileUpdate(TileEntity tile)
    {
        super(PacketTypeHandler.TILE_UPDATE, false);
        this.tile = tile;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        NBTTagCompound compound = new NBTTagCompound();
        tile.writeToNBT(compound);
        compound.setName("compound");
        NBTTagByteArray.writeNamedTag(compound, data);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        compound = (NBTTagCompound) NBTTagByteArray.readNamedTag(data);
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        EntityPlayer thePlayer = (EntityPlayer) player;
        this.tile = thePlayer.getEntityWorld().getBlockTileEntity(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
        if (tile != null)
        {
            tile.readFromNBT(compound);
        }
    }
}
