package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.item.ModItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class EventHandlerClient
{
    @SubscribeEvent
    public void FOVEvent(FOVUpdateEvent event)
    {
        if (Minecraft.getMinecraft().thePlayer.getItemInUse() != null && Minecraft.getMinecraft().thePlayer.getItemInUse().getItem() == ModItems.playerRelocator)
        {
            ModItems.playerRelocator.shiftFOV(Minecraft.getMinecraft().thePlayer.getItemInUse(), event);
        }
    }

    @SubscribeEvent
    public void overlayEvent(RenderGameOverlayEvent event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.HELMET)
        {
            if (Minecraft.getMinecraft().thePlayer.getItemInUse() != null && Minecraft.getMinecraft().thePlayer.getItemInUse().getItem() == ModItems.playerRelocator)
            {
                ModItems.playerRelocator.renderBlur(Minecraft.getMinecraft().thePlayer.getItemInUse(), event.resolution);
            }
        }
    }
}
