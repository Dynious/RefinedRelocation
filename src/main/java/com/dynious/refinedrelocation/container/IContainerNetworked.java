package com.dynious.refinedrelocation.container;

import net.minecraft.entity.player.EntityPlayer;

public interface IContainerNetworked
{
    void onMessageAction(int messageId, EntityPlayer player);
    void onMessageString(int messageId, String value, EntityPlayer player);
    void onMessageByte(int messageId, byte value, EntityPlayer player);
    void onMessageDouble(int messageId, double value, EntityPlayer player);
    void onMessageInteger(int messageId, int value, EntityPlayer player);
    void onMessageBoolean(int messageId, boolean value, EntityPlayer player);
    void onMessageBooleanArray(int messageId, boolean[] values, EntityPlayer player);
}
