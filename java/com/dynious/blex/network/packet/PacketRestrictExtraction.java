package com.dynious.blex.network.packet;

import com.dynious.blex.gui.container.IContainerAdvancedFiltered;
import com.dynious.blex.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketRestrictExtraction extends PacketTile
{
    boolean restrictExtraction;

    public PacketRestrictExtraction()
    {
        super(PacketTypeHandler.RESTRICT_EXTRACTION, false);
    }

    public PacketRestrictExtraction(boolean restrictExtraction)
    {
        super(PacketTypeHandler.RESTRICT_EXTRACTION, false);
        this.restrictExtraction = restrictExtraction;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeBoolean(restrictExtraction);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        restrictExtraction = data.readBoolean();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null || !(container instanceof IContainerAdvancedFiltered))
            return;

        ((IContainerAdvancedFiltered) container).setRestrictExtraction(restrictExtraction);
    }
}
