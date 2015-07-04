package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.IMultiFilterTile;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterType;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIBoolean;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIByte;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class ContainerFiltered extends ContainerHierarchical implements IContainerFiltered
{

    public IFilterTileGUI tile;

    private boolean lastBlacklist = true;
    private int lastPriority;
    private boolean initialUpdate = true;

    public ContainerFiltered(IFilterTileGUI tile)
    {
        this.tile = tile;
    }

    public ContainerFiltered(IFilterTileGUI tile, ContainerHierarchical parentContainer)
    {
        super(parentContainer);

        this.tile = tile;
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (tile.getFilter().isDirty())
        {
            initialUpdate = true;
            tile.getFilter().markDirty(false);
        }

        for (int i = 0; i < tile.getFilter().getFilterCount(); i++)
        {
            IMultiFilterChild filterChild = tile.getFilter().getFilterAtIndex(i);
            if (initialUpdate || filterChild.isDirty())
            {
                for (Object crafter : crafters)
                {
                    if (initialUpdate)
                    {
                        NetworkHandler.INSTANCE.sendTo(new MessageSetFilterType(i, filterChild.getTypeName()), (EntityPlayerMP) crafter);
                    }
                    filterChild.sendUpdate((EntityPlayerMP) crafter);
                }
                filterChild.markDirty(false);
            }
        }

        if (tile.getFilter().isBlacklisting() != lastBlacklist || initialUpdate)
        {
            sendSyncMessage(new MessageGUIBoolean(MessageGUI.BLACKLIST, tile.getFilter().isBlacklisting()));
            lastBlacklist = tile.getFilter().isBlacklisting();
        }

        if (tile instanceof ISortingInventory)
        {
            ISortingInventory inv = (ISortingInventory) tile;
            if (inv.getPriority().ordinal() != lastPriority || initialUpdate)
            {
                sendSyncMessage(new MessageGUIByte(MessageGUI.PRIORITY, (byte) inv.getPriority().ordinal()));
                lastPriority = inv.getPriority().ordinal();
            }
        }

        if (initialUpdate)
        {
            initialUpdate = false;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void setBlackList(boolean value)
    {
        lastBlacklist = value;
        tile.getFilter().setBlacklists(value);
    }

    @Override
    public void setPriority(int priority)
    {
        lastPriority = priority;
        if (tile instanceof ISortingInventory)
        {
            ((ISortingInventory) tile).setPriority(ISortingInventory.Priority.values()[priority]);
        }
    }

    @Override
    public void onMessageByte(int messageId, byte value, EntityPlayer player)
    {
        if(!checkPermission(player)) {
            return;
        }
        if(messageId == MessageGUI.PRIORITY) {
            setPriority(value);
        }
    }

    @Override
    public void onMessageBoolean(int messageId, boolean value, EntityPlayer player)
    {
        if(!checkPermission(player)) {
            return;
        }
        if(messageId == MessageGUI.BLACKLIST) {
            setBlackList(value);
        }
    }

    @Override
    public IFilterGUI getFilter()
    {
        return tile.getFilter();
    }
}
