package com.dynious.blex.helper;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.lib.GuiIds;
import com.dynious.blex.tileentity.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.FMLNetworkHandler;
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
            else if (tile instanceof TileFilteringChest || (Loader.isModLoaded("IronChest") && tile instanceof TileFilteringIronChest))
            {
                guiId = GuiIds.FILTERING_CHEST;
            }

            if (guiId != -1)
            {
                FMLNetworkHandler.openGui(player, BlockExtenders.instance, guiId, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
                return true;
            }
            else
                return false;
        }
        return true;
    }
}
