package com.dynious.refinedrelocation;

import com.dynious.refinedrelocation.api.IAPIHandler;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingMemberHandler;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.lib.GuiIds;
import com.dynious.refinedrelocation.grid.FilterStandard;
import com.dynious.refinedrelocation.grid.sorting.SortingInventoryHandler;
import com.dynious.refinedrelocation.grid.sorting.SortingMemberHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class APIHandler implements IAPIHandler
{
    public static APIHandler instance = new APIHandler();

    public Object getModInstance()
    {
        return RefinedRelocation.instance;
    }

    public int getFilteringGUIID()
    {
        return GuiIds.FILTERED;
    }

    public IFilterGUI createStandardFilter(IFilterTileGUI filterTile)
    {
        return new FilterStandard(filterTile);
    }

    public ISortingMemberHandler createSortingMemberHandler(TileEntity owner)
    {
        return new SortingMemberHandler(owner);
    }

    public ISortingInventoryHandler createSortingInventoryHandler(TileEntity owner)
    {
        return new SortingInventoryHandler(owner);
    }

    public void registerRelocatorModule(String identifier, Class<? extends IRelocatorModule> clazz) throws IllegalArgumentException
    {
        RelocatorModuleRegistry.add(identifier, clazz);
    }

    public void openRelocatorModuleGUI(IItemRelocator relocator, EntityPlayer player, int side)
    {
        player.openGui(RefinedRelocation.instance, GuiIds.RELOCATOR_FILTER_BASE + side, relocator.getTileEntity().getWorldObj(), relocator.getTileEntity().xCoord, relocator.getTileEntity().yCoord, relocator.getTileEntity().zCoord);
    }
}
