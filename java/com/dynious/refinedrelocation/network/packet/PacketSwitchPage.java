package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerSortingImporter;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketSwitchPage extends CustomPacket
{
    private boolean previous;

    public PacketSwitchPage()
    {
        super(PacketTypeHandler.SWITCH_PAGE, false);
    }

    public PacketSwitchPage(boolean previous)
    {
        super(PacketTypeHandler.SWITCH_PAGE, false);
        this.previous = previous;
    }
    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeBoolean(previous);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        previous = data.readBoolean();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof ContainerSortingImporter))
            return;

        if (previous)
        {
            ((ContainerSortingImporter) container).previousPage();
        }
        else
        {
            ((ContainerSortingImporter) container).nextPage();
        }
    }

}
