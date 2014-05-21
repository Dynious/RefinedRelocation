package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerPowerLimiter;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketSetMaxPower implements IPacket
{
    public double amount;

    public PacketSetMaxPower()
    {
    }

    public PacketSetMaxPower(double amount)
    {
        this.amount = amount;
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeDouble(amount);
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        amount = bytes.readDouble();

        Container container = player.openContainer;

        if (container == null || !(container instanceof ContainerPowerLimiter))
            return;

        ((ContainerPowerLimiter) container).setMaxAcceptedEnergy(amount);
    }
}
