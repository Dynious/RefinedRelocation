package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerAdvanced;
import com.dynious.refinedrelocation.gui.container.ContainerAdvancedFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerFiltered;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketRedstoneEnabled extends CustomPacket
{
    boolean redstoneEnabled = true;

    public PacketRedstoneEnabled()
    {
        super(PacketTypeHandler.REDSTONE_ENABLED, false);
    }

    public PacketRedstoneEnabled(boolean redstoneEnabled)
    {
        super(PacketTypeHandler.REDSTONE_ENABLED, false);
        this.redstoneEnabled = redstoneEnabled;
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        super.writeData(data);
        data.writeBoolean(redstoneEnabled);
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        super.readData(data);
        redstoneEnabled = data.readBoolean();
    }

    @Override
    public void execute(INetworkManager manager, Player player)
    {
        super.execute(manager, player);

        Container container = ((EntityPlayer) player).openContainer;

        if (container == null)
            return;

        // TODO: a better way to get the container tile
        TileEntity tile = null;
        if (container instanceof ContainerAdvanced)
            tile = (TileEntity) ((ContainerAdvanced) container).tile;
        if (container instanceof ContainerAdvancedFiltered)
            tile = (TileEntity) ((ContainerAdvancedFiltered) container).tile;
        if (container instanceof ContainerFiltered)
            tile = (TileEntity) ((ContainerFiltered) container).tile;

        if (tile == null || !(tile instanceof TileBlockExtender))
            return;

        ((TileBlockExtender) tile).setRedstoneTransmissionEnabled(redstoneEnabled);
    }
}
