package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerModuleSneaky;
import com.dynious.refinedrelocation.gui.container.ContainerModuleSneakyExtraction;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketExtractionSide extends CustomPacket
{
    int side;

    public PacketExtractionSide()
    {
        super(PacketTypeHandler.EXTRACTION_SIDE, false);
    }

    public PacketExtractionSide(int side)
    {
        super(PacketTypeHandler.EXTRACTION_SIDE, false);
        this.side = side;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeByte(side);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        side = data.readByte();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof ContainerModuleSneakyExtraction))
            return;

        ((ContainerModuleSneakyExtraction) container).setExtractionSide(side);
    }
}
