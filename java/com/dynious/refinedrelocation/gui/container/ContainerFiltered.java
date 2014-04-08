package com.dynious.refinedrelocation.gui.container;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.lib.GuiNetworkIds;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.network.packet.PacketUserFilter;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerFiltered extends ContainerHierarchical implements IContainerFiltered
{

    public IFilterTileGUI tile;

    private String lastUserFilter = "";
    private boolean lastBlacklist = true;
    private boolean lastFilterOptions[];
    private boolean initialUpdate = true;

    public ContainerFiltered(IFilterTileGUI tile)
    {
        this.tile = tile;

        lastFilterOptions = new boolean[tile.getFilter().getSize()];
    }

    public ContainerFiltered(IFilterTileGUI tile, ContainerHierarchical parentContainer)
    {
        super(parentContainer);

        this.tile = tile;

        lastFilterOptions = new boolean[tile.getFilter().getSize()];
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (!tile.getFilter().getUserFilter().equals(lastUserFilter) || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                if (crafter instanceof EntityPlayer)
                {
                    PacketDispatcher.sendPacketToPlayer(PacketTypeHandler.populatePacket(new PacketUserFilter(tile.getFilter().getUserFilter())), ((Player) crafter));
                }
            }
            lastUserFilter = tile.getFilter().getUserFilter();
        }

        for (int i = 0; i < tile.getFilter().getSize(); i++)
        {
            if (tile.getFilter().getValue(i) != lastFilterOptions[i] || initialUpdate)
            {
                for (Object crafter : crafters)
                {
                    ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), GuiNetworkIds.FILTERED_BASE + (tile.getFilter().getValue(i) ? 0 : 1), i);
                }
                lastFilterOptions[i] = tile.getFilter().getValue(i);
            }
        }

        if (tile.getFilter().isBlacklisting() != lastBlacklist || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(getTopMostContainer(), GuiNetworkIds.FILTERED_BASE + 2, tile.getFilter().isBlacklisting() ? 1 : 0);
            }
            lastBlacklist = tile.getFilter().isBlacklisting();
        }

        if (initialUpdate)
            initialUpdate = false;
    }

    @Override
    public void updateProgressBar(int id, int value)
    {
        id -= GuiNetworkIds.FILTERED_BASE;

        if (id > GuiNetworkIds.FILTERED_MAX || id < GuiNetworkIds.FILTERED_BASE)
            return;

        switch (id)
        {
            case 0:
                setFilterOption(value, true);
                break;
            case 1:
                setFilterOption(value, false);
                break;
            case 2:
                setBlackList(value != 0);
                break;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void setUserFilter(String filter)
    {
        lastUserFilter = filter;
        tile.getFilter().setUserFilter(filter);
    }

    @Override
    public void setBlackList(boolean value)
    {
        lastBlacklist = value;
        tile.getFilter().setBlacklists(value);
    }

    @Override
    public void setFilterOption(int filterIndex, boolean value)
    {
        lastFilterOptions[filterIndex] = value;
        tile.getFilter().setValue(filterIndex, value);
    }

    @Override
    public void toggleFilterOption(int filterIndex)
    {
        if (filterIndex >= 0 && filterIndex < lastFilterOptions.length)
            this.setFilterOption(filterIndex, !tile.getFilter().getValue(filterIndex));
    }

}
