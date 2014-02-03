package com.dynious.blex.network.packet;

import com.dynious.blex.gui.container.IContainerFiltered;
import com.dynious.blex.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketFilterOption extends CustomPacket
{
    public byte filterIndex;

    public PacketFilterOption()
    {
        super(PacketTypeHandler.FILTER_OPTION, false);
    }

    public PacketFilterOption(byte filterIndex)
    {
        super(PacketTypeHandler.FILTER_OPTION, false);
        this.filterIndex = filterIndex;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.write(filterIndex);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        filterIndex = data.readByte();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer)player).openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return;

        ((IContainerFiltered) container).toggleFilterOption( filterIndex );
    }
}
