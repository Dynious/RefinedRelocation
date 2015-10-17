package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIBoolean;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIByte;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAdvanced extends ContainerHierarchical implements IContainerAdvanced {
    public IAdvancedTile tile;

    private boolean lastSpreadItems = false;
    private byte[] lastInsertDirection = {1, 1, 1, 1, 1, 1, 1};
    private byte lastMaxStackSize = 64;
    private boolean initialUpdate = true;

    public ContainerAdvanced(IAdvancedTile tile) {
        this.tile = tile;
    }

    public ContainerAdvanced(IAdvancedTile tile, ContainerHierarchical parentContainer) {
        super(parentContainer);
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < tile.getInsertDirection().length; i++) {
            if (tile.getInsertDirection()[i] != lastInsertDirection[i] || initialUpdate) {
                sendSyncMessage(new MessageGUIByte(MessageGUI.DIRECTIONS_START + i, tile.getInsertDirection()[i]));
                lastInsertDirection[i] = tile.getInsertDirection()[i];
            }
        }

        if (tile.getMaxStackSize() != lastMaxStackSize || initialUpdate) {
            sendSyncMessage(new MessageGUIByte(MessageGUI.MAX_STACK_SIZE, tile.getMaxStackSize()));
            lastMaxStackSize = tile.getMaxStackSize();
        }

        if (tile.getSpreadItems() != lastSpreadItems || initialUpdate) {
            sendSyncMessage(new MessageGUIBoolean(MessageGUI.SPREAD_ITEMS, tile.getSpreadItems()));
            lastSpreadItems = tile.getSpreadItems();
        }

        if (initialUpdate) {
            initialUpdate = false;
        }
    }

    @Override
    public void setInsertDirection(int from, int value) {
        tile.setInsertDirection(from, value);
        lastInsertDirection[from] = (byte) value;
    }

    @Override
    public void setMaxStackSize(byte maxStackSize) {
        tile.setMaxStackSize(maxStackSize);
        lastMaxStackSize = maxStackSize;
    }

    @Override
    public void setSpreadItems(boolean spreadItems) {
        tile.setSpreadItems(spreadItems);
        lastSpreadItems = spreadItems;
    }

    @Override
    public void onMessageBoolean(int messageId, boolean value, EntityPlayer player, Side side) {
        if (isRestrictedAccessWithError(player)) {
            return;
        }
        switch (messageId) {
            case MessageGUI.SPREAD_ITEMS:
                setSpreadItems(value);
                break;
            case MessageGUI.REDSTONE_ENABLED:
                if (tile instanceof TileBlockExtender) {
                    ((TileBlockExtender) tile).setRedstoneTransmissionEnabled(value);
                }
                break;
        }
    }

    @Override
    public void onMessageByte(int messageId, byte value, EntityPlayer player, Side side) {
        if (isRestrictedAccessWithError(player)) {
            return;
        }
        if(messageId >= MessageGUI.DIRECTIONS_START && messageId <= MessageGUI.DIRECTIONS_END) {
            setInsertDirection(messageId - MessageGUI.DIRECTIONS_START, value);
            return;
        }
        switch (messageId) {
            case MessageGUI.MAX_STACK_SIZE:
                setMaxStackSize(value);
                break;
        }
    }

}
