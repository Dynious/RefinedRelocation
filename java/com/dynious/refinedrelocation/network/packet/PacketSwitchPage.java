package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerSortingImporter;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketSwitchPage implements IPacket
{
    private boolean previous;

    public PacketSwitchPage()
    {
    }

    public PacketSwitchPage(boolean previous)
    {
        this.previous = previous;
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeBoolean(previous);
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        previous = bytes.readBoolean();

        Container container = player.openContainer;

        if (container == null || !(container instanceof ContainerSortingImporter))
            return;

        if (previous)
        {
            ((ContainerSortingImporter) container).previousPage();
        }
        else
        {
            ((ContainerSortingImporter) container).nextPage();
        }
    }
}
