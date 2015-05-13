package com.dynious.refinedrelocation.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public abstract class ContainerRefinedRelocation extends Container implements IContainerNetworked {

    @Override
    public void onMessage(int messageId, Object value, EntityPlayer player) {}

}
