package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerPowerLimiter;
import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketRedstoneToggle extends CustomPacket
{
    boolean toggle = true;

    public PacketRedstoneToggle()
    {
        super(PacketTypeHandler.REDSTONE_TOGGLE, false);
    }

    public PacketRedstoneToggle(boolean toggle)
    {
        super(PacketTypeHandler.REDSTONE_TOGGLE, false);
        this.toggle = toggle;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeBoolean(toggle);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        toggle = data.readBoolean();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof ContainerPowerLimiter))
            return;

        ((ContainerPowerLimiter) container).setRedstoneToggle(toggle);
    }
}
