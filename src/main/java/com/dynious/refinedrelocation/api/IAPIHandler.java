package com.dynious.refinedrelocation.api;

import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.api.tileentity.IMultiFilterTile;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingMemberHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public interface IAPIHandler
{
    Object getModInstance();

    int getFilteringGUIID();

    IMultiFilter createStandardFilter(IMultiFilterTile filterTile);

    ISortingMemberHandler createSortingMemberHandler(TileEntity owner);

    ISortingInventoryHandler createSortingInventoryHandler(TileEntity owner);

    void registerRelocatorModule(String identifier, Class<? extends IRelocatorModule> clazz) throws IllegalArgumentException;

    void openRelocatorModuleGUI(IItemRelocator relocator, EntityPlayer player, int side);

    void registerToolboxClazz(Class clazz);

    ItemStack insert(TileEntity tile, ItemStack itemStack, ForgeDirection side, boolean simulate);

    void registerMultiFilterChild(String identifier, Class<? extends IMultiFilterChild> clazz);
}
