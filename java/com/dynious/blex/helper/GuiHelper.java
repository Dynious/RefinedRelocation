package com.dynious.blex.helper;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.lib.GuiIds;
import com.dynious.blex.tileentity.*;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class GuiHelper
{
    public static void openGui(EntityPlayer player, TileEntity tile)
    {
        if (!tile.getWorldObj().isRemote)
        {
            int GuiId = -1;

            if (tile instanceof TileAdvancedBlockExtender)
            {
                GuiId = GuiIds.ADVANCED_BLOCK_EXTENDER;
            }
            else if (tile instanceof TileFilteredBlockExtender)
            {
                GuiId = GuiIds.FILTERED;
            }
            else if (tile instanceof TileWirelessBlockExtender)
            {
                GuiId = GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER;
            }
            else if (tile instanceof TileAdvancedFilteredBlockExtender)
            {
                GuiId = GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER;
            }
            else if (tile instanceof TileAdvancedBuffer)
            {
                GuiId = GuiIds.ADVANCED_BUFFER;
            }
            else if (tile instanceof TileFilteredBuffer)
            {
                GuiId = GuiIds.FILTERED;
            }
            else if (tile instanceof TileFilteringChest)
            {
                GuiId = GuiIds.FILTERED_CHEST;
            }

            if (GuiId != -1)
                FMLNetworkHandler.openGui(player, BlockExtenders.instance, GuiId, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
        }
    }
}
