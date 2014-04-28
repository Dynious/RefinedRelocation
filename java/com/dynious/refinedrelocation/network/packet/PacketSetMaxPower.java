package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerPowerLimiter;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketSetMaxPower extends CustomPacket
{
    public double amount;

    public PacketSetMaxPower()
    {
        super(PacketTypeHandler.SET_MAX_POWER, false);
    }

    public PacketSetMaxPower(double amount)
    {
        super(PacketTypeHandler.SET_MAX_POWER, false);
        this.amount = amount;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeDouble(amount);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        amount = data.readDouble();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof ContainerPowerLimiter))
            return;

        ((ContainerPowerLimiter) container).setMaxAcceptedEnergy(amount);
    }
}
