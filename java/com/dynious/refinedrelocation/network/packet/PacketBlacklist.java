package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketBlacklist implements IPacket
{
    boolean isBlackList = true;

    public PacketBlacklist()
    {
    }

    public PacketBlacklist(boolean isBlackList)
    {
        this.isBlackList = isBlackList;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        bytes.writeBoolean(isBlackList);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        isBlackList = bytes.readBoolean();

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return;

        ((IContainerFiltered) container).setBlackList(isBlackList);
    }
}
