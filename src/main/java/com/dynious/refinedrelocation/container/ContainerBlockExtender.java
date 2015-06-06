package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBlockExtender extends ContainerHierarchical
{
    public TileBlockExtender tile;

    public ContainerBlockExtender(TileBlockExtender tile)
    {
        this.tile = tile;
    }

    public ContainerBlockExtender(TileBlockExtender tile, ContainerHierarchical parentContainer)
    {
        super(parentContainer);
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void onMessage(int messageId, Object value, EntityPlayer player)
    {
        switch (messageId)
        {
            case MessageGUI.REDSTONE_ENABLED:
                tile.setRedstoneTransmissionEnabled((Boolean) value);
                break;
        }
    }
}
