package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.command.KongaHandler;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.compat.waila.PlayerRelocatorBaseHUDHandler;
import com.dynious.refinedrelocation.compat.waila.RelocatorHUDHandler;
import com.dynious.refinedrelocation.util.VersionChecker;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

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
                PlayerRelocatorBaseHUDHandler.tick++;
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
                        ChatComponentText prefixComponent = new ChatComponentText("[Refined Relocation] ");
                        prefixComponent.getChatStyle().setColor(EnumChatFormatting.GOLD);
                        ChatComponentTranslation versionComponent = new ChatComponentTranslation(Strings.NEW_VERSION, VersionChecker.getRemoteVersion().getModVersion());
                        versionComponent.getChatStyle().setColor(EnumChatFormatting.WHITE);
                        prefixComponent.appendSibling(versionComponent);
                        Minecraft.getMinecraft().thePlayer.addChatMessage(prefixComponent);
                        ChatComponentTranslation clickComponent = new ChatComponentTranslation(Strings.NEW_VERSION_CLICK);
                        clickComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/RefinedRelocation latest"));
                        clickComponent.getChatStyle().setColor(EnumChatFormatting.YELLOW);
                        Minecraft.getMinecraft().thePlayer.addChatMessage(clickComponent);
                    }
                }
            }
            KongaHandler.checkDownloadedAndPlay();
        }
    }
}
