package com.dynious.refinedrelocation.helper;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.GuiIds;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.gui.*;
import com.dynious.refinedrelocation.tileentity.*;
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
            } else if (tile instanceof TileFilteredBlockExtender)
            {
                guiId = GuiIds.FILTERED;
            } else if (tile instanceof TileWirelessBlockExtender)
            {
                guiId = GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER;
            } else if (tile instanceof TileAdvancedFilteredBlockExtender)
            {
                guiId = GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER;
            } else if (tile instanceof TileBlockExtender)
            {
                guiId = GuiIds.BLOCK_EXTENDER;
            } else if (tile instanceof TileAdvancedBuffer)
            {
                guiId = GuiIds.ADVANCED_BUFFER;
            } else if (tile instanceof TileFilteredBuffer)
            {
                guiId = GuiIds.FILTERED;
            } else if (tile instanceof TileSortingChest)
            {
                guiId = GuiIds.SORTING_CHEST;
            } else if (tile instanceof TileSortingInputPane)
            {
                guiId = GuiIds.SORTING_INPUT_PANE;
            } else if (tile instanceof TilePowerLimiter)
            {
                guiId = GuiIds.POWER_LIMITER;
            }

            if (guiId != -1)
            {
                player.openGui(RefinedRelocation.instance, guiId, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
                return true;
            } else
                return false;
        }
        return true;
    }

    public static void sendByteMessage(int messageId, byte value)
    {
        NetworkHandler.INSTANCE.sendToServer(new MessageGUIByte(messageId, value));
    }

    public static void sendIntMessage(int messageId, int value)
    {
        NetworkHandler.INSTANCE.sendToServer(new MessageGUIInteger(messageId, value));
    }

    public static void sendBooleanMessage(int messageId, boolean value)
    {
        NetworkHandler.INSTANCE.sendToServer(new MessageGUIBoolean(messageId, value));
    }

    public static void sendStringMessage(int messageId, String value)
    {
        NetworkHandler.INSTANCE.sendToServer(new MessageGUIString(messageId, value));
    }

    public static void sendActionMessage(int messageId)
    {
        NetworkHandler.INSTANCE.sendToServer(new MessageGUIAction(messageId));
    }

    public static void sendDoubleMessage(int messageId, double value)
    {
        NetworkHandler.INSTANCE.sendToServer(new MessageGUIDouble(messageId, value));
    }
}
