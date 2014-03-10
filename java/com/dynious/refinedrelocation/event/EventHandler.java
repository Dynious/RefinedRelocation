package com.dynious.refinedrelocation.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventHandler
{
    @SubscribeEvent
    public void playerNameEvent(PlayerEvent.NameFormat event)
    {
        if (event.username.equals("redmen800"))
        {
            event.displayname = "Dynious";
        }
    }
}
