package com.dynious.refinedrelocation.container;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

public interface IContainerNetworked
{
    void onMessageAction(int messageId, EntityPlayer player, Side side);
    void onMessageString(int messageId, String value, EntityPlayer player, Side side);
    void onMessageByte(int messageId, byte value, EntityPlayer player, Side side);
    void onMessageDouble(int messageId, double value, EntityPlayer player, Side side);
    void onMessageInteger(int messageId, int value, EntityPlayer player, Side side);
    void onMessageBoolean(int messageId, boolean value, EntityPlayer player, Side side);
    void onMessageBooleanArray(int messageId, boolean[] values, EntityPlayer player, Side side);
}
