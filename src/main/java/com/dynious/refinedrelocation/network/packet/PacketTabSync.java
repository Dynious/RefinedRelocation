package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.sorting.FilterStandard;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketTabSync implements IPacket
{
    public String[] labels;

    public PacketTabSync()
    {
    }

    public PacketTabSync(String[] labels)
    {
        this.labels = labels;
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(labels.length);
        for (String label : labels)
        {
            ByteBufUtils.writeUTF8String(bytes, label);
        }
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        labels = new String[bytes.readInt()];
        for (int i = 0; i < labels.length; i++)
        {
            labels[i] = ByteBufUtils.readUTF8String(bytes);
        }

        FilterStandard.syncTabs(labels);
    }
}
