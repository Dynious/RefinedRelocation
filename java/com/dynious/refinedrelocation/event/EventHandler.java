package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.item.ModItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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
