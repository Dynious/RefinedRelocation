package com.dynious.blex.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.gui.*;
import com.dynious.blex.gui.container.*;
import com.dynious.blex.tileentity.*;
import com.dynious.blex.lib.GuiIds;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {

    public GuiHandler() {
        NetworkRegistry.instance().registerGuiHandler(BlockExtenders.instance, this);
    }

    @Override
    public Object getServerGuiElement(int GuiId, EntityPlayer player, World world, int x, int y, int z) {
        switch (GuiId) {
            //case GuiIds.BLOCK_EXTENDER:
            //case GuiIds.BUFFER:
            case GuiIds.FILTERED_BLOCK_EXTENDER:
            case GuiIds.FILTERED_BUFFER:
                return new ContainerFiltered( player.inventory, (IFilterTile)world.getBlockTileEntity(x, y, z) );
            case GuiIds.ADVANCED_BLOCK_EXTENDER:
            case GuiIds.ADVANCED_BUFFER:
                return new ContainerAdvanced( player.inventory, (IAdvancedTile)world.getBlockTileEntity(x, y, z) );
            case GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER:
            case GuiIds.WIRELESS_BLOCK_EXTENDER:
                return new ContainerAdvancedFiltered( player.inventory, (IAdvancedFilteredTile)world.getBlockTileEntity(x, y, z) );
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int GuiId, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        switch (GuiId) {
            case GuiIds.ADVANCED_BLOCK_EXTENDER:
                if (tile != null && tile instanceof TileAdvancedBlockExtender) {
                    return new GuiAdvancedBlockExtender( player.inventory, (TileAdvancedBlockExtender)tile );
                }
                break;
            case GuiIds.FILTERED_BLOCK_EXTENDER:
                if (tile != null && tile instanceof TileFilteredBlockExtender) {
                    return new GuiFiltered( player.inventory, (TileFilteredBlockExtender)tile );
                }
                break;
            case GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER:
                if (tile != null && tile instanceof TileAdvancedFilteredBlockExtender) {
                    return new GuiAdvancedFilteredBlockExtender( player.inventory, (TileAdvancedFilteredBlockExtender)tile );
                }
                break;
            case GuiIds.WIRELESS_BLOCK_EXTENDER:
                if (tile != null && tile instanceof TileWirelessBlockExtender) {
                    return new GuiWirelessBlockExtender( player.inventory, (TileWirelessBlockExtender)tile );
                }
                break;
            case GuiIds.FILTERED_BUFFER:
                if (tile != null && tile instanceof TileFilteredBuffer) {
                    return new GuiFiltered( player.inventory, (TileFilteredBuffer)tile );
                }
                break;
            case GuiIds.ADVANCED_BUFFER:
                if (tile != null && tile instanceof TileAdvancedBuffer) {
                    return new GuiAdvancedBuffer( player.inventory, (TileAdvancedBuffer)tile );
                }
                break;
        }

        return null;
    }

}
