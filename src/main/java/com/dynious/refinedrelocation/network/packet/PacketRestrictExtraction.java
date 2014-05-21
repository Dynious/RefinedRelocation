package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerAdvancedFiltered;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketRestrictExtraction implements IPacket
{
    boolean restrictExtraction;

    public PacketRestrictExtraction()
    {
    }

    public PacketRestrictExtraction(boolean restrictExtraction)
    {
        this.restrictExtraction = restrictExtraction;
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        restrictExtraction = bytes.readBoolean();

        Container container = player.openContainer;

        if (container == null || !(container instanceof IContainerAdvancedFiltered))
            return;

        ((IContainerAdvancedFiltered) container).setRestrictExtraction(restrictExtraction);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeBoolean(restrictExtraction);
    }
}
