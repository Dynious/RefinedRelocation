package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.network.NetworkHelper;
import com.dynious.refinedrelocation.network.packet.PacketTabSync;
import com.dynious.refinedrelocation.sorting.FilterStandard;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class EventHandler
{
    @SubscribeEvent
    public void loggedInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            NetworkHelper.sendTo(new PacketTabSync(FilterStandard.getLabels()), event.player);
        }
    }
}
