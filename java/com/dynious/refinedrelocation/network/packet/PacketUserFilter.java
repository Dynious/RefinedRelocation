package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketUserFilter implements IPacket
{
    public String userFilter;

    public PacketUserFilter()
    {
    }

    public PacketUserFilter(String userFilter)
    {
        this.userFilter = userFilter;
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        userFilter = ByteBufUtils.readUTF8String(bytes);

        Container container = player.openContainer;

        if (!(container instanceof IContainerFiltered))
            return;

        ((IContainerFiltered) container).setUserFilter(userFilter);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        ByteBufUtils.writeUTF8String(bytes, userFilter);
    }
}
