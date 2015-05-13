package com.dynious.refinedrelocation.container;

import net.minecraft.entity.player.EntityPlayer;

public interface IContainerNetworked {
    void onMessage(int messageId, Object value, EntityPlayer player);
}
