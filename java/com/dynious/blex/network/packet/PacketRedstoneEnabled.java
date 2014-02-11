package com.dynious.blex.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import com.dynious.blex.gui.container.ContainerAdvanced;
import com.dynious.blex.gui.container.ContainerAdvancedFiltered;
import com.dynious.blex.gui.container.ContainerFiltered;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.tileentity.TileBlockExtender;
import cpw.mods.fml.common.network.Player;

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
        // (maybe create a ContainerBlExBase that holds the tile entity and have all other containers extend that?)
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
