package com.dynious.blex.network;

import com.dynious.blex.lib.Reference;
import com.dynious.blex.network.packet.*;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public enum PacketTypeHandler
{
    MAX_STACK_SIZE(PacketMaxStackSize.class),
    INSERT_DIRECTION(PacketInsertDirection.class),
    SPREAD_ITEMS(PacketSpread.class),
    USER_FILTER(PacketUserFilter.class),
    BLACKLIST(PacketBlacklist.class),
    FILTER_OPTION(PacketFilterOption.class),
    RESTRICT_EXTRACTION(PacketRestrictExtraction.class),
    TILE_UPDATE(PacketTileUpdate.class);

    private Class<? extends CustomPacket> clazz;

    PacketTypeHandler(Class<? extends CustomPacket> clazz)
    {
        this.clazz = clazz;
    }

    public static CustomPacket buildPacket(byte[] data)
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        int selector = bis.read();
        DataInputStream dis = new DataInputStream(bis);

        CustomPacket packet = null;

        try
        {
            packet = values()[selector].clazz.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }

        packet.readPopulate(dis);

        return packet;
    }

    public static CustomPacket buildPacket(PacketTypeHandler type)
    {
        CustomPacket packet = null;

        try
        {
            packet = values()[type.ordinal()].clazz.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }

        return packet;
    }

    public static Packet populatePacket(CustomPacket packetCustom)
    {
        byte[] data = packetCustom.populate();

        Packet250CustomPayload packet250 = new Packet250CustomPayload();
        packet250.channel = Reference.cannelName;
        packet250.data = data;
        packet250.length = data.length;
        packet250.isChunkDataPacket = packetCustom.isChunkDataPacket;

        return packet250;
    }
}
