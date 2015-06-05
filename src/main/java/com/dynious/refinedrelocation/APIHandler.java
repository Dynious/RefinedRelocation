package com.dynious.refinedrelocation;

import com.dynious.refinedrelocation.api.IAPIHandler;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingMemberHandler;
import com.dynious.refinedrelocation.grid.MultiFilter;
import com.dynious.refinedrelocation.grid.filter.MultiFilterRegistry;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.grid.sorting.SortingInventoryHandler;
import com.dynious.refinedrelocation.grid.sorting.SortingMemberHandler;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.item.ItemToolBox;
import com.dynious.refinedrelocation.lib.GuiIds;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

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
        return new MultiFilter(filterTile);
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
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
            player.openGui(RefinedRelocation.instance, GuiIds.RELOCATOR_FILTER_BASE + side, relocator.getTileEntity().getWorldObj(), relocator.getTileEntity().xCoord, relocator.getTileEntity().yCoord, relocator.getTileEntity().zCoord);
    }

    public void registerToolboxClazz(Class clazz)
    {
        ItemToolBox.addToolboxClass(clazz);
    }

    @Override
    public ItemStack insert(TileEntity tile, ItemStack itemStack, ForgeDirection side, boolean simulate)
    {
        return IOHelper.insert(tile, itemStack, side, simulate);
    }

    @Override
    public void registerMultiFilterChild(String identifier, Class<? extends IMultiFilterChild> clazz) {
        MultiFilterRegistry.add(identifier, clazz);
    }
}
