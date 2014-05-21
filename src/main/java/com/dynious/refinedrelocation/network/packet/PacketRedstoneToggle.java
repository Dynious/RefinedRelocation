package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerPowerLimiter;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketRedstoneToggle implements IPacket
{
    boolean toggle = true;

    public PacketRedstoneToggle()
    {
    }

    public PacketRedstoneToggle(boolean toggle)
    {
        this.toggle = toggle;
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeBoolean(toggle);
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        toggle = bytes.readBoolean();

        Container container = player.openContainer;

        if (container == null || !(container instanceof ContainerPowerLimiter))
            return;

        ((ContainerPowerLimiter) container).setRedstoneToggle(toggle);
    }
}
