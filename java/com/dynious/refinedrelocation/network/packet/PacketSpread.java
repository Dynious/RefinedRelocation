package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerAdvanced;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketSpread implements IPacket
{
    boolean spreadItems = false;

    public PacketSpread()
    {
    }

    public PacketSpread(boolean spreadItems)
    {
        this.spreadItems = spreadItems;
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        spreadItems = bytes.readBoolean();

        Container container = player.openContainer;

        if (container == null || !(container instanceof IContainerAdvanced))
            return;

        ((IContainerAdvanced) container).setSpreadItems(spreadItems);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeBoolean(spreadItems);
    }
}
