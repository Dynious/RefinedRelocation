package com.dynious.blex.network.packet;

import com.dynious.blex.gui.container.IContainerAdvanced;
import com.dynious.blex.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketMaxStackSize extends CustomPacket
{
    public byte amount;

    public PacketMaxStackSize()
    {
        super(PacketTypeHandler.MAX_STACK_SIZE, false);
    }

    public PacketMaxStackSize(byte amount)
    {
        super(PacketTypeHandler.MAX_STACK_SIZE, false);
        this.amount = amount;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.write(amount);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        amount = data.readByte();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer)player).openContainer;

        if (container == null || !(container instanceof IContainerAdvanced))
            return;

        ((IContainerAdvanced) container).setMaxStackSize( amount );
    }
}
