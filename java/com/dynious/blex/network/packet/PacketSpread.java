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

public class PacketSpread extends CustomPacket
{
    boolean spreadItems = false;

    public PacketSpread()
    {
        super(PacketTypeHandler.SPREAD_ITEMS, false);
    }

    public PacketSpread(boolean spreadItems)
    {
        super(PacketTypeHandler.SPREAD_ITEMS, false);
        this.spreadItems = spreadItems;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeBoolean(spreadItems);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        spreadItems = data.readBoolean();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof IContainerAdvanced))
            return;

        ((IContainerAdvanced) container).setSpreadItems(spreadItems);
    }
}
