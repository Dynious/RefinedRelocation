package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIBoolean;
import com.dynious.refinedrelocation.tileentity.IAdvancedFilteredTile;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAdvancedFiltered extends ContainerHierarchical implements IContainerAdvancedFiltered {

    public IAdvancedFilteredTile tile;

    // delegates
    private IContainerFiltered containerFiltered;
    private IContainerAdvanced containerAdvanced;

    private boolean lastRestrictExtraction = false;
    private boolean initialUpdate = true;

    public ContainerAdvancedFiltered(IAdvancedFilteredTile tile) {
        this.tile = tile;

        this.containerFiltered = new ContainerFiltered(tile, this);
        this.containerAdvanced = new ContainerAdvanced(tile, this);
    }

    public ContainerAdvancedFiltered(IAdvancedFilteredTile tile, ContainerHierarchical parentContainer) {
        super(parentContainer);

        this.tile = tile;

        this.containerFiltered = new ContainerFiltered(tile, this);
        this.containerAdvanced = new ContainerAdvanced(tile, this);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        ((ContainerAdvanced) containerAdvanced).detectAndSendChanges();
        ((ContainerFiltered) containerFiltered).detectAndSendChanges();

        if (tile.getRestrictExtraction() != lastRestrictExtraction || initialUpdate) {
            sendSyncMessage(new MessageGUIBoolean(MessageGUI.FILTERED_EXTRACTION, tile.getRestrictExtraction()));
            lastRestrictExtraction = tile.getRestrictExtraction();
        }

        if (initialUpdate) {
            initialUpdate = false;
        }
    }

    @Override
    public void setRestrictExtraction(boolean restrictExtraction) {
        tile.setRestrictionExtraction(restrictExtraction);
        lastRestrictExtraction = restrictExtraction;
    }

    @Override
    public void setPriority(int priority) {
    }

    @Override
    public IFilterGUI getFilter() {
        return tile.getFilter();
    }

    @Override
    public void setInsertDirection(int from, int value) {
        containerAdvanced.setInsertDirection(from, value);
    }

    @Override
    public void setMaxStackSize(byte maxStackSize) {
        containerAdvanced.setMaxStackSize(maxStackSize);
    }

    @Override
    public void setSpreadItems(boolean spreadItems) {
        containerAdvanced.setSpreadItems(spreadItems);
    }
    // end delegate methods


    @Override
    public void onMessageByte(int messageId, byte value, EntityPlayer player, Side side) {
        if (isRestrictedAccessWithError(player)) {
            return;
        }
        if (messageId >= MessageGUI.DIRECTIONS_START && messageId <= MessageGUI.DIRECTIONS_END) {
            setInsertDirection(messageId - MessageGUI.DIRECTIONS_START, value);
            return;
        }
        if (messageId == MessageGUI.MAX_STACK_SIZE) {
            setMaxStackSize(value);
        }
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
            case MessageGUI.FILTERED_EXTRACTION:
                setRestrictExtraction(value);
                break;
            case MessageGUI.REDSTONE_ENABLED:
                if (tile instanceof TileBlockExtender) {
                    ((TileBlockExtender) tile).setRedstoneTransmissionEnabled(value);
                }
                break;
        }
    }

}
