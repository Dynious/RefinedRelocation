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

public class PacketUserFilter extends CustomPacket
{
    public String userFilter;

    public PacketUserFilter()
    {
        super(PacketTypeHandler.USER_FILTER, false);
    }

    public PacketUserFilter(String userFilter)
    {
        super(PacketTypeHandler.USER_FILTER, false);
        this.userFilter = userFilter;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeUTF(userFilter);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        userFilter = data.readUTF();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (!(container instanceof IContainerFiltered))
            return;

        ((IContainerFiltered) container).setUserFilter(userFilter);
    }
}
