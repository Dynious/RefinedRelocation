package com.dynious.refinedrelocation.network.packet;

import com.dynious.refinedrelocation.gui.container.ContainerAdvanced;
import com.dynious.refinedrelocation.gui.container.ContainerAdvancedFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerFiltered;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class MessageRedstoneEnabled implements IMessage, IMessageHandler<MessageRedstoneEnabled, IMessage>
{
    boolean redstoneEnabled = true;

    public MessageRedstoneEnabled()
    {
    }

    public MessageRedstoneEnabled(boolean redstoneEnabled)
    {
        this.redstoneEnabled = redstoneEnabled;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        redstoneEnabled = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(redstoneEnabled);
    }

    @Override
    public IMessage onMessage(MessageRedstoneEnabled message, MessageContext ctx)
    {
        Container container = ctx.getServerHandler().playerEntity.openContainer;

        if (container == null)
            return null;

        // TODO: a better way to get the container tile
        TileEntity tile = null;
        if (container instanceof ContainerAdvanced)
            tile = (TileEntity) ((ContainerAdvanced) container).tile;
        if (container instanceof ContainerAdvancedFiltered)
            tile = (TileEntity) ((ContainerAdvancedFiltered) container).tile;
        if (container instanceof ContainerFiltered)
            tile = (TileEntity) ((ContainerFiltered) container).tile;

        if (tile == null || !(tile instanceof TileBlockExtender))
            return null;

        ((TileBlockExtender) tile).setRedstoneTransmissionEnabled(message.redstoneEnabled);
        return null;
    }
}
