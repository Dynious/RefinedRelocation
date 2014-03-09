package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerAdvanced;
import com.dynious.refinedrelocation.gui.container.ContainerAdvancedFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerFiltered;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class PacketRedstoneEnabled implements IPacket
{
    boolean redstoneEnabled = true;

    public PacketRedstoneEnabled()
    {
    }

    public PacketRedstoneEnabled(boolean redstoneEnabled)
    {
        this.redstoneEnabled = redstoneEnabled;
    }

    @Override
    public void readBytes(ByteBuf bytes, EntityPlayer player)
    {
        redstoneEnabled = bytes.readBoolean();

        Container container = player.openContainer;

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

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeBoolean(redstoneEnabled);
    }
}
