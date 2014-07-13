package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.command.KongaHandler;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.version.VersionChecker;
import com.dynious.refinedrelocation.mods.waila.RelocatorHUDHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

public class TickEvent
{

    private static boolean checkedVersion = false;

    @SubscribeEvent
    public void onClientTick(cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent event)
    {
        if (event.phase == cpw.mods.fml.common.gameevent.TickEvent.Phase.END)
        {
            if (Mods.IS_WAILA_LOADED)
            {
                RelocatorHUDHandler.tick++;
                if (RelocatorHUDHandler.tick == RelocatorHUDHandler.TICKS_BETWEEN_STUFFED_ITEM_UPDATE)
                {
                    RelocatorHUDHandler.stuffedItems = null;
                }
            }

            if (!VersionChecker.sentIMCMessage && !checkedVersion && Settings.DISPLAY_VERSION_RESULT && FMLClientHandler.instance().getClient().currentScreen == null)
            {
                if (VersionChecker.getResult() != VersionChecker.CheckState.UNINITIALIZED)
                {
                    checkedVersion = true;

                    if (VersionChecker.getResult() == VersionChecker.CheckState.OUTDATED)
                    {
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.OUTDATED, Reference.VERSION, VersionChecker.getRemoteVersion().getModVersion())));
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(Strings.CHANGE_LOG) + ":"));
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(" " + VersionChecker.getRemoteVersion().getChangeLog()));
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(Strings.LATEST)));
                    }
                }
            }
            KongaHandler.checkDownloadedAndPlay();
        }
    }
}
