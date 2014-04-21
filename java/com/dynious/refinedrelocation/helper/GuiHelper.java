package com.dynious.refinedrelocation.helper;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.GuiIds;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class GuiHelper
{
    public static boolean openGui(EntityPlayer player, TileEntity tile)
    {
        if (!tile.getWorldObj().isRemote)
        {
            int guiId = -1;

            if (tile instanceof TileAdvancedBlockExtender)
            {
                guiId = GuiIds.ADVANCED_BLOCK_EXTENDER;
            }
            else if (tile instanceof TileFilteredBlockExtender)
            {
                guiId = GuiIds.FILTERED;
            }
            else if (tile instanceof TileWirelessBlockExtender)
            {
                guiId = GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER;
            }
            else if (tile instanceof TileAdvancedFilteredBlockExtender)
            {
                guiId = GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER;
            }
            else if (tile instanceof TileAdvancedBuffer)
            {
                guiId = GuiIds.ADVANCED_BUFFER;
            }
            else if (tile instanceof TileFilteredBuffer)
            {
                guiId = GuiIds.FILTERED;
            }
            else if (tile instanceof TileSortingChest || (Loader.isModLoaded("IronChest") && tile instanceof TileSortingIronChest))
            {
                guiId = GuiIds.SORTING_CHEST;
            }
            else if (tile instanceof TileSortingImporter)
            {
                guiId = GuiIds.SORTING_IMPORTER;
            }

            if (guiId != -1)
            {
                player.openGui(RefinedRelocation.instance, guiId, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
                return true;
            }
            else
                return false;
        }
        return true;
    }
}
