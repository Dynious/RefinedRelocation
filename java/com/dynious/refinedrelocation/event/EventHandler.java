package com.dynious.refinedrelocation.event;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventHandler
{
    @ForgeSubscribe
    public void playerNameEvent(PlayerEvent.NameFormat event)
    {
        if (event.username.equals("redmen800"))
        {
            event.displayname = "Dynious";
        }
    }
}
