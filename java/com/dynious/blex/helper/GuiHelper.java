package com.dynious.blex.helper;

import com.dynious.blex.gui.*;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketInsertDirection;
import com.dynious.blex.network.packet.PacketTileUpdate;
import com.dynious.blex.tileentity.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.tileentity.TileEntity;

public class GuiHelper
{
    public static void openGui(TileEntity tile, Player player)
    {
        if (!tile.getWorldObj().isRemote)
        {
            PacketDispatcher.sendPacketToPlayer(PacketTypeHandler.populatePacket(new PacketTileUpdate(tile)), player);
        }
        else
        {
            if (tile instanceof TileAdvancedBlockExtender)
            {
                FMLCommonHandler.instance().showGuiScreen(new GuiAdvancedBlockExtender((TileAdvancedBlockExtender) tile));
            }
            else if (tile instanceof TileFilteredBlockExtender)
            {
                FMLCommonHandler.instance().showGuiScreen(new GuiFiltered((TileFilteredBlockExtender) tile));
            }
            else if (tile instanceof TileWirelessBlockExtender)
            {
                FMLCommonHandler.instance().showGuiScreen(new GuiWirelessBlockExtender((TileWirelessBlockExtender) tile));
            }
            else if (tile instanceof TileAdvancedFilteredBlockExtender)
            {
                FMLCommonHandler.instance().showGuiScreen(new GuiAdvancedFilteredBlockExtender((TileAdvancedFilteredBlockExtender) tile));
            }

            else if (tile instanceof TileAdvancedBuffer)
            {
                FMLCommonHandler.instance().showGuiScreen(new GuiAdvancedBuffer((TileAdvancedBuffer) tile));
            }
            else if (tile instanceof TileFilteredBuffer)
            {
                FMLCommonHandler.instance().showGuiScreen(new GuiFiltered((TileFilteredBuffer) tile));
            }
        }
    }
}
