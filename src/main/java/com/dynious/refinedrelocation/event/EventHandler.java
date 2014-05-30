package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.grid.FilterStandard;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageTabSync;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class EventHandler
{
    @SubscribeEvent
    public void loggedInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            NetworkHandler.INSTANCE.sendTo(new MessageTabSync(FilterStandard.getLabels()), (EntityPlayerMP) event.player);
        }
    }
}
