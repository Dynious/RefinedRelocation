package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketBlacklist implements IPacket
{
    boolean isBlackList = true;

    public PacketBlacklist()
    {
    }

    public PacketBlacklist(boolean isBlackList)
    {
        this.isBlackList = isBlackList;
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        isBlackList = bytes.readBoolean();

        Container container = player.openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return;

        ((IContainerFiltered) container).setBlackList(isBlackList);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeBoolean(isBlackList);
    }
}
