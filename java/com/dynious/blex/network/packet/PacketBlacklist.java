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

public class PacketBlacklist extends CustomPacket
{
    boolean isBlackList = true;

    public PacketBlacklist()
    {
        super(PacketTypeHandler.BLACKLIST, false);
    }

    public PacketBlacklist(boolean isBlackList)
    {
        super(PacketTypeHandler.BLACKLIST, false);
        this.isBlackList = isBlackList;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeBoolean(isBlackList);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        isBlackList = data.readBoolean();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof IContainerFiltered))
            return;

        ((IContainerFiltered) container).setBlackList(isBlackList);
    }
}
