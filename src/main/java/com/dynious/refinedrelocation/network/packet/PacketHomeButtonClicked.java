package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerMultiModule;
import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketHomeButtonClicked extends CustomPacket
{
    int index;

    public PacketHomeButtonClicked()
    {
        super(PacketTypeHandler.PRIORITY, false);
    }

    public PacketHomeButtonClicked(int index)
    {
        super(PacketTypeHandler.INDEX, false);
        this.index = index;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeByte(index);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        index = data.readByte();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof ContainerMultiModule))
            return;

        ((ContainerMultiModule) container).openOrActive(index);
    }
}
