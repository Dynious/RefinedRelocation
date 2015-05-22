package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.grid.filter.AbstractFilter;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIAction;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIBoolean;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIByte;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class ContainerFiltered extends ContainerHierarchical implements IContainerFiltered {

    public IFilterTileGUI tile;

    private boolean lastBlacklist = true;
    private int lastPriority;
    private boolean initialUpdate = true;

    public ContainerFiltered(IFilterTileGUI tile) {
        this.tile = tile;
    }

    public ContainerFiltered(IFilterTileGUI tile, ContainerHierarchical parentContainer) {
        super(parentContainer);

        this.tile = tile;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if(tile.getFilter().isDirty()) {
            initialUpdate = true;
            tile.getFilter().markDirty(false);
        }

        for(int i = 0; i < tile.getFilter().getFilterCount(); i++) {
            AbstractFilter filter = tile.getFilter().getFilterAtIndex(i);
            if(initialUpdate || filter.isDirty()) {
                for(Object crafter : crafters) {
                    if(crafter instanceof EntityPlayerMP) {
                        if(initialUpdate) {
                            filter.sendInitialUpdate((EntityPlayerMP) crafter);
                        } else {
                            filter.sendUpdate((EntityPlayerMP) crafter);
                        }
                    }
                }
                filter.markDirty(false);
            }
        }

        if(tile.getFilter().isBlacklisting() != lastBlacklist || initialUpdate) {
            for(Object crafter : crafters) {
                NetworkHandler.INSTANCE.sendTo(new MessageGUIBoolean(MessageGUI.BLACKLIST, tile.getFilter().isBlacklisting()), (EntityPlayerMP) crafter);
            }
            lastBlacklist = tile.getFilter().isBlacklisting();
        }

        if(tile instanceof ISortingInventory) {
            ISortingInventory inv = (ISortingInventory) tile;
            if(inv.getPriority().ordinal() != lastPriority || initialUpdate) {
                for(Object crafter : crafters) {
                    NetworkHandler.INSTANCE.sendTo(new MessageGUIByte(MessageGUI.PRIORITY, (byte) inv.getPriority().ordinal()), (EntityPlayerMP) crafter);
                }
                lastPriority = inv.getPriority().ordinal();
            }
        }

        if(initialUpdate) {
            initialUpdate = false;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void setBlackList(boolean value) {
        lastBlacklist = value;
        tile.getFilter().setBlacklists(value);
    }

    @Override
    public void setPriority(int priority) {
        lastPriority = priority;
        if (tile instanceof ISortingInventory) {
            ((ISortingInventory) tile).setPriority(ISortingInventory.Priority.values()[priority]);
        }
    }

    @Override
    public void onMessage(int messageId, Object value, EntityPlayer player) {
        switch(messageId) {
            case MessageGUI.BLACKLIST: setBlackList((Boolean) value); break;
            case MessageGUI.PRIORITY: setPriority((Byte) value); break;
            case MessageGUI.REDSTONE_ENABLED:
                if(tile instanceof TileBlockExtender) {
                    ((TileBlockExtender) tile).setRedstoneTransmissionEnabled((Boolean) value);
                }
                break;
        }
    }

    @Override
    public IFilterGUI getFilter() {
        return tile.getFilter();
    }
}
