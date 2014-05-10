package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketLabeledFilterOption extends CustomPacket
{
    public String label;

    public PacketLabeledFilterOption()
    {
        super(PacketTypeHandler.LABELED_FILTER_OPTION, false);
    }

    public PacketLabeledFilterOption(String label)
    {
        super(PacketTypeHandler.LABELED_FILTER_OPTION, false);
        this.label = label;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeUTF(label);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        label = data.readUTF();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return;

        ((IContainerFiltered) container).toggleFilterOption(label);
    }
}
