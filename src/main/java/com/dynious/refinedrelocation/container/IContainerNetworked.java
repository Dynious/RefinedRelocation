// Copyright (c) 2014, Christopher "blay09" Baker
// All rights reserved.
package com.dynious.refinedrelocation.container;

import net.minecraft.entity.player.EntityPlayer;

public interface IContainerNetworked {
    void onMessage(int messageId, Object value, EntityPlayer player);
}
