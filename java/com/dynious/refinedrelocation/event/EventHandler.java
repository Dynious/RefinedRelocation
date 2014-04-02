package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.helper.LogHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
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

    @ForgeSubscribe
    public void FOVEvent(FOVUpdateEvent event)
    {
        if (Minecraft.getMinecraft().thePlayer.getItemInUse() != null && Minecraft.getMinecraft().thePlayer.getItemInUse().getItem().itemID == ModItems.playerRelocator.itemID)
        {
            ModItems.playerRelocator.shiftFOV(Minecraft.getMinecraft().thePlayer.getItemInUse(), event);
        }
    }

    @ForgeSubscribe
    public void onSoundLoad(SoundLoadEvent event)
    {
        for (String soundFile : Sounds.sounds)
        {
            try
            {
                event.manager.addSound(soundFile);
            }
            catch (Exception e)
            {
                LogHelper.warning("Failed loading sound file: " + soundFile);
            }
        }
    }

    @ForgeSubscribe
    public void overlayEvent(RenderGameOverlayEvent event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.HELMET)
        {
            if (Minecraft.getMinecraft().thePlayer.getItemInUse() != null && Minecraft.getMinecraft().thePlayer.getItemInUse().getItem().itemID == ModItems.playerRelocator.itemID)
            {
                ModItems.playerRelocator.renderBlur(Minecraft.getMinecraft().thePlayer.getItemInUse(), event.resolution);
            }
        }
    }
}
