package com.dynious.blex.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

import com.dynious.blex.lib.GuiNetworkIds;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketUserFilter;
import com.dynious.blex.tileentity.IFilterTile;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ContainerFiltered extends ContainerHierarchical implements IContainerFiltered {

    public IFilterTile tile;
    
    private String lastUserFilter = "";
    private boolean lastBlacklist = true;
    private boolean lastFilterOptions[];
    private boolean initialUpdate = true;
    
    public ContainerFiltered(InventoryPlayer invPlayer, IFilterTile tile) {
        this.tile = tile;
        
        lastFilterOptions = new boolean[tile.getFilter().getSize()];
    }

    public ContainerFiltered(InventoryPlayer invPlayer, IFilterTile tile, ContainerHierarchical parentContainer) {
        super(parentContainer);
        
        this.tile = tile;
        
        lastFilterOptions = new boolean[tile.getFilter().getSize()];
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        
        if (!tile.getFilter().userFilter.equals(lastUserFilter) || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                if (crafter instanceof EntityPlayer)
                {
                    PacketDispatcher.sendPacketToPlayer(PacketTypeHandler.populatePacket(new PacketUserFilter(tile.getFilter().userFilter)), ((Player)crafter));
                }
            }
            lastUserFilter = tile.getFilter().userFilter;
        }
        
        for (int i=0; i<tile.getFilter().getSize(); i++)
        {
            if (tile.getFilter().getValue(i) != lastFilterOptions[i] || initialUpdate)
            {
                for (Object crafter : crafters)
                {
                    ((ICrafting)crafter).sendProgressBarUpdate(getTopMostContainer(), GuiNetworkIds.FILTERED_BASE + (tile.getFilter().getValue(i) ? 0 : 1), i);
                }
                lastFilterOptions[i] = tile.getFilter().getValue(i);
            }
        }
        
        if (tile.getBlackList() != lastBlacklist || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting)crafter).sendProgressBarUpdate(getTopMostContainer(), GuiNetworkIds.FILTERED_BASE + 2, tile.getBlackList() ? 1 : 0);
            }
            lastBlacklist = tile.getBlackList();
        }
        
        if (initialUpdate)
            initialUpdate = false;
    }
    
    @Override
    public void updateProgressBar(int id, int value) {
        id -= GuiNetworkIds.FILTERED_BASE;
        
        if (id > GuiNetworkIds.FILTERED_MAX || id < GuiNetworkIds.FILTERED_BASE)
            return;
        
        switch( id )
        {
        case 0:
            setFilterOption( value, true );
            break;
        case 1:
            setFilterOption( value, false );
            break;
        case 2:
            setBlackList( value != 0 );
            break;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void setUserFilter(String filter) {
        lastUserFilter = filter;
        tile.getFilter().userFilter = filter;
    }

    @Override
    public void setBlackList(boolean value) {
        lastBlacklist = value;
        tile.setBlackList( value );
    }

    @Override
    public void setFilterOption(int filterIndex, boolean value) {
        lastFilterOptions[ filterIndex ] = value;
        tile.getFilter().setValue(filterIndex, value);
    }
    
    @Override
    public void toggleFilterOption(int filterIndex) {
        this.setFilterOption( filterIndex, !tile.getFilter().getValue(filterIndex) );
    }
    
}
