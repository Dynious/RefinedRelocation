package com.dynious.refinedrelocation.network.packet.gui;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public abstract class MessageGUI implements IMessage
{

    public static final int SPREAD_ITEMS = 1;
    public static final int MAX_STACK_SIZE = 2;
    public static final int FILTERED_EXTRACTION = 3;
    public static final int CHECK_METADATA = 4;
    public static final int CHECK_NBT = 5;
    public static final int PRIORITY = 6;
    public static final int REDSTONE_TOGGLE = 7;
    public static final int POWER_LIMIT = 8;
    public static final int REDSTONE_ENABLED = 9;
    public static final int ENERGY_TYPES = 10;
    public static final int DIRECTIONS_START = 11;
    public static final int DIRECTIONS_END = 16;
    public static final int REMOVE_MODULE = 17;
    public static final int OPEN_MODULE = 18;

    protected int id = 0;

    public MessageGUI()
    {
    }

    public MessageGUI(int id)
    {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(id);
    }

}
