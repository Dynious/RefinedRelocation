package com.dynious.refinedrelocation.api;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IRelocatorModule;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingMemberHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class APIUtils
{
    private static IAPIHandler apiHandler;

    static
    {
        try
        {
            Class c = Class.forName("com.dynious.refinedrelocation.APIHandler");
            apiHandler = (IAPIHandler) c.getField("instance").get(c);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Filtering GUI for the TileEntity at the given position.
     * The TileEntity should implement IFilterTileGUI.
     */
    public static void openFilteringGUI(EntityPlayer entityPlayer, World world, int x, int y, int z)
    {
        entityPlayer.openGui(apiHandler.getModInstance(), apiHandler.getFilteringGUIID(), world, x, y, z);
    }

    /**
     * Creates a new instance of the standard IFilterGUI used for Sorting Chests & Filtered BlockExtenders/Buffers etc.
     * If you want the IFilterGUI settings to be saved call writeToNBT(...) and readFromNBT(...).
     *
     * @return a new instance of the standard IFilterGUI
     */
    public static IFilterGUI createStandardFilter(IFilterTileGUI filterTile)
    {
        return apiHandler.createStandardFilter(filterTile);
    }

    /**
     * Creates a new SortingMemberHandler instance. Use with ISortingMember implementers.
     *
     * @param owner The TileEntity this SortingMemberHandler will be used with.
     * @return a new SortingMemberHandler instance.
     */
    public static ISortingMemberHandler createSortingMemberHandler(TileEntity owner)
    {
        return apiHandler.createSortingMemberHandler(owner);
    }

    /**
     * Creates a new SortingInventoryHandler instance. Use with ISortingInventory implementers.
     *
     * @param owner The TileEntity this SortingInventoryHandler will be used with.
     * @return a new SortingInventoryHandler instance.
     */
    public static ISortingInventoryHandler createSortingInventoryHandler(TileEntity owner)
    {
        return apiHandler.createSortingInventoryHandler(owner);
    }

    /**
     * Registers a filter for attachment to relocators. To correctly save the filter your
     * filter MUST be registered here.
     *
     * @param identifier The identifier of this filter
     * @param clazz The class of this filter
     */
    public static void registerRelocatorFilter(String identifier, Class<? extends IRelocatorModule> clazz) throws IllegalArgumentException
    {
        apiHandler.registerRelocatorFilter(identifier, clazz);
    }

    /**
     * Will open a GUI for the filter by calling the getGUI() and getContainer() methods in your filter.
     *
     * @param relocator The identifier of this filter
     * @param player The Player that the GUI should open for
     * @param side The side of the filter that should open a GUI
     */
    public static void openRelocatorFilterGUI(IRelocator relocator, EntityPlayer player, int side)
    {
        apiHandler.openRelocatorFilterGUI(relocator, player, side);
    }
}
