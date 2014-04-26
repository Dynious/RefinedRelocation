package com.dynious.refinedrelocation.network;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.gui.*;
import com.dynious.refinedrelocation.gui.container.*;
import com.dynious.refinedrelocation.lib.GuiIds;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.ironchest.client.GUIChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
    public GuiHandler()
    {
        NetworkRegistry.instance().registerGuiHandler(RefinedRelocation.instance, this);
    }

    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null)
        {
            switch (guiId)
            {
                case GuiIds.FILTERED:
                    if (tile instanceof IFilterTileGUI)
                    {
                        return new ContainerFiltered((IFilterTileGUI) tile);
                    }
                    break;
                case GuiIds.ADVANCED_BLOCK_EXTENDER:
                case GuiIds.ADVANCED_BUFFER:
                    if (tile instanceof IAdvancedTile)
                    {
                        return new ContainerAdvanced((IAdvancedTile) tile);
                    }
                    break;
                case GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER:
                    if (tile instanceof IAdvancedFilteredTile)
                    {
                        return new ContainerAdvancedFiltered((IAdvancedFilteredTile) tile);
                    }
                    break;
                case GuiIds.SORTING_CHEST:
                    if (Loader.isModLoaded("IronChest") && tile instanceof TileSortingIronChest)
                    {
                        return new ContainerSortingIronChest(player, (TileSortingIronChest) tile, ((TileSortingIronChest) tile).getType(), 0, 0);
                    }
                    else if (tile instanceof TileSortingChest)
                    {
                        return new ContainerSortingChest(player, (TileSortingChest) tile);
                    }
                    break;
                case GuiIds.FILTERING_HOPPER:
                    if (tile instanceof TileFilteringHopper)
                    {
                        return new ContainerFilteringHopper(player.inventory, (IFilterTileGUI) tile);
                    }
                    break;
                case GuiIds.SORTING_IMPORTER:
                    if (tile instanceof TileSortingImporter)
                    {
                        return new ContainerSortingImporter(player, (TileSortingImporter) tile);
                    }
                    break;
                case GuiIds.POWER_LIMITER:
                    if (tile instanceof TilePowerLimiter)
                    {
                        return new ContainerPowerLimiter((TilePowerLimiter) tile);
                    }
                    break;
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null)
        {
            switch (guiId)
            {
                case GuiIds.ADVANCED_BLOCK_EXTENDER:
                    if (tile instanceof TileAdvancedBlockExtender)
                    {
                        return new GuiAdvancedBlockExtender(player.inventory, (TileAdvancedBlockExtender) tile);
                    }
                    break;
                case GuiIds.FILTERED:
                    if (tile instanceof IFilterTileGUI)
                    {
                        return new GuiFiltered(player.inventory, (IFilterTileGUI) tile);
                    }
                    break;
                case GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER:
                    if (tile instanceof TileAdvancedFilteredBlockExtender)
                    {
                        return new GuiAdvancedFilteredBlockExtender(player.inventory, (TileAdvancedFilteredBlockExtender) tile);
                    }
                    break;
                case GuiIds.ADVANCED_BUFFER:
                    if (tile instanceof TileAdvancedBuffer)
                    {
                        return new GuiAdvancedBuffer(player.inventory, (TileAdvancedBuffer) tile);
                    }
                    break;
                case GuiIds.SORTING_CHEST:
                    if (Loader.isModLoaded("IronChest") && tile instanceof TileSortingIronChest)
                    {
                        return new GuiSortingIronChest(GUIChest.GUI.values()[((TileSortingIronChest) tile).getType().ordinal()], player, (TileSortingIronChest) tile);
                    }
                    else if (tile instanceof TileSortingChest)
                    {
                        return new GuiSortingChest(player, (TileSortingChest) tile);
                    }
                    break;
                case GuiIds.FILTERING_HOPPER:
                    if (tile instanceof TileFilteringHopper)
                    {
                        return new GuiFilteringHopper(player.inventory, (TileFilteringHopper) tile);
                    }
                    break;
                case GuiIds.SORTING_IMPORTER:
                    if (tile instanceof TileSortingImporter)
                    {
                        return new GuiSortingImporter(player, (TileSortingImporter) tile);
                    }
                    break;
                case GuiIds.POWER_LIMITER:
                    if (tile instanceof TilePowerLimiter)
                    {
                        return new GuiPowerLimiter((TilePowerLimiter) tile);
                    }
                    break;
            }
        }
        return null;
    }

}
