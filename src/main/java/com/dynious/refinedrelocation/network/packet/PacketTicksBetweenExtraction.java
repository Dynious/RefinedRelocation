package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerModuleExtraction;
import com.dynious.refinedrelocation.gui.container.IContainerAdvanced;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketTicksBetweenExtraction extends CustomPacket
{
    public int amount;

    public PacketTicksBetweenExtraction()
    {
        super(PacketTypeHandler.MAX_STACK_SIZE, false);
    }

    public PacketTicksBetweenExtraction(int amount)
    {
        super(PacketTypeHandler.TICKS_BETWEEN_EXT, false);
        this.amount = amount;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeInt(amount);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        amount = data.readInt();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof ContainerModuleExtraction))
            return;

        ((ContainerModuleExtraction) container).setTicksBetweenExtraction(amount);
    }
}
