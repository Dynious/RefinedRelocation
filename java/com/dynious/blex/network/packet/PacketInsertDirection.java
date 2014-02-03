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

public class PacketInsertDirection extends CustomPacket
{
    public byte sideAndDirection;

    public PacketInsertDirection()
    {
        super(PacketTypeHandler.INSERT_DIRECTION, false);
    }

    public PacketInsertDirection(byte side, byte direction)
    {
        super(PacketTypeHandler.INSERT_DIRECTION, false);
        this.sideAndDirection = (byte) ((byte) (side << 4) | (byte) (direction));
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.write(sideAndDirection);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        sideAndDirection = data.readByte();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof IContainerAdvanced))
            return;

        ((IContainerAdvanced) container).setInsertDirection(sideAndDirection >> 4, sideAndDirection & 15); // 15 = 0b1111
    }
}
