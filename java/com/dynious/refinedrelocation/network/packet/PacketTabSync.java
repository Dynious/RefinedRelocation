package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.sorting.FilterStandard;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketTabSync extends CustomPacket
{
    public String[] labels;

    public PacketTabSync()
    {
        super(PacketTypeHandler.TAB_SYNC, false);
    }

    public PacketTabSync(String[] labels)
    {
        super(PacketTypeHandler.TAB_SYNC, false);
        this.labels = labels;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeInt(labels.length);
        for (String label : labels)
        {
            data.writeUTF(label);
        }
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        labels = new String[data.readInt()];
        for (int i = 0; i < labels.length; i++)
        {
            labels[i] = data.readUTF();
        }
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        FilterStandard.syncTabs(labels);
    }
}
