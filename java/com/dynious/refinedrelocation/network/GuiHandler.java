package com.dynious.refinedrelocation.network;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.gui.*;
import com.dynious.refinedrelocation.gui.container.ContainerAdvanced;
import com.dynious.refinedrelocation.gui.container.ContainerAdvancedFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerSortingChest;
import com.dynious.refinedrelocation.gui.container.ContainerFilteringHopper;
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
    public Object getServerGuiElement(int GuiId, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        switch (GuiId)
        {
            //case GuiIds.BLOCK_EXTENDER:
            //case GuiIds.BUFFER:
            case GuiIds.FILTERED:
                return new ContainerFiltered((IFilterTileGUI) tile);
            case GuiIds.ADVANCED_BLOCK_EXTENDER:
            case GuiIds.ADVANCED_BUFFER:
                return new ContainerAdvanced((IAdvancedTile) tile);
            case GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER:
                return new ContainerAdvancedFiltered((IAdvancedFilteredTile) tile);
            case GuiIds.FILTERING_CHEST:
                if (Loader.isModLoaded("IronChest") && tile != null && tile instanceof TileSortingIronChest)
                {
                    return GuiSortingIronChest.makeContainer(GUIChest.GUI.values()[((TileSortingIronChest)tile).getType().ordinal()], player, (TileSortingIronChest) tile);
                }
                return new ContainerSortingChest(player, (TileSortingChest) tile);
            case GuiIds.FILTERING_HOPPER:
                return new ContainerFilteringHopper(player.inventory, (IFilterTileGUI) tile);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int GuiId, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        switch (GuiId)
        {
            case GuiIds.ADVANCED_BLOCK_EXTENDER:
                if (tile != null && tile instanceof TileAdvancedBlockExtender)
                {
                    return new GuiAdvancedBlockExtender(player.inventory, (TileAdvancedBlockExtender) tile);
                }
                break;
            case GuiIds.FILTERED:
                if (tile != null && tile instanceof IFilterTileGUI)
                {
                    return new GuiFiltered(player.inventory, (IFilterTileGUI) tile);
                }
                break;
            case GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER:
                if (tile != null && tile instanceof TileAdvancedFilteredBlockExtender)
                {
                    return new GuiAdvancedFilteredBlockExtender(player.inventory, (TileAdvancedFilteredBlockExtender) tile);
                }
                break;
            case GuiIds.ADVANCED_BUFFER:
                if (tile != null && tile instanceof TileAdvancedBuffer)
                {
                    return new GuiAdvancedBuffer(player.inventory, (TileAdvancedBuffer) tile);
                }
                break;
            case GuiIds.FILTERING_CHEST:
                if (Loader.isModLoaded("IronChest") && tile != null && tile instanceof TileSortingIronChest)
                {
                    return new GuiSortingIronChest(GUIChest.GUI.values()[((TileSortingIronChest)tile).getType().ordinal()], player, (TileSortingIronChest) tile);
                }

                if (tile != null && tile instanceof TileSortingChest)
                {
                    return new GuiSortingChest(player, (TileSortingChest) tile);
                }
                break;
            case GuiIds.FILTERING_HOPPER:
                if (tile != null && tile instanceof TileFilteringHopper)
                {
                    return new GuiFilteringHopper(player.inventory, (TileFilteringHopper) tile);
                }
                break;
        }

        return null;
    }

}
