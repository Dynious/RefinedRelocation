// Copyright (c) 2014, Christopher "blay09" Baker
// All rights reserved.
package com.dynious.refinedrelocation.network.packet.gui;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public abstract class MessageGUI implements IMessage {

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
