package com.dynious.refinedrelocation.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public abstract class ContainerRefinedRelocation extends Container implements IContainerNetworked {

    @Override
    public void onMessageAction(int messageId, EntityPlayer player) {}

    @Override
    public void onMessageString(int messageId, String value, EntityPlayer player) {}

    @Override
    public void onMessageByte(int messageId, byte value, EntityPlayer player) {}

    @Override
    public void onMessageDouble(int messageId, double value, EntityPlayer player) {}

    @Override
    public void onMessageInteger(int messageId, int value, EntityPlayer player) {}

    @Override
    public void onMessageBoolean(int messageId, boolean value, EntityPlayer player) {}

    @Override
    public void onMessageBooleanArray(int messageId, boolean[] values, EntityPlayer player) {}
}
