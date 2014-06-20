package com.dynious.refinedrelocation.api;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingMemberHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public interface IAPIHandler
{
    public Object getModInstance();

    public int getFilteringGUIID();

    public IFilterGUI createStandardFilter(IFilterTileGUI filterTile);

    public ISortingMemberHandler createSortingMemberHandler(TileEntity owner);

    public ISortingInventoryHandler createSortingInventoryHandler(TileEntity owner);

    public void registerRelocatorFilter(String identifier, Class<? extends IRelocatorModule> clazz) throws IllegalArgumentException;

    public void openRelocatorFilterGUI(IItemRelocator relocator, EntityPlayer player, int side);
}
