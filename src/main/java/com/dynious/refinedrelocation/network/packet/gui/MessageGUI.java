// Copyright (c) 2014, Christopher "blay09" Baker
// All rights reserved.
package com.dynious.refinedrelocation.network.packet.gui;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public abstract class MessageGUI implements IMessage {

    public static final int BLACKLIST = 0;
    public static final int SPREAD_ITEMS = 1;
    public static final int MAX_STACK_SIZE = 2;
    public static final int RESTRICT_EXTRACTION = 3;
    public static final int FILTER_OPTION = 4;
    public static final int USERFILTER = 5;
    public static final int PRIORITY = 6;
    public static final int REDSTONE_TOGGLE = 7;
    public static final int POWER_LIMIT = 8;

    protected int id = 0;

    public MessageGUI() {
    }

    public MessageGUI(int id) {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(id);
    }

}
