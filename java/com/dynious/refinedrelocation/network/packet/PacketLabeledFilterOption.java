package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketLabeledFilterOption implements IPacket
{
    public String label;

    public PacketLabeledFilterOption()
    {
    }

    public PacketLabeledFilterOption(String label)
    {
        this.label = label;
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        label = ByteBufUtils.readUTF8String(bytes);

        Container container = player.openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return;

        ((IContainerFiltered) container).toggleFilterOption(label);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        ByteBufUtils.writeUTF8String(bytes, label);
    }
}
