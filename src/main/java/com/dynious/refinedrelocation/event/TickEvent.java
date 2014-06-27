package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.version.VersionChecker;
import com.dynious.refinedrelocation.mods.waila.RelocatorHUDHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.StatCollector;

import java.util.EnumSet;

public class TickEvent implements ITickHandler
{

    private static boolean initialized = false;

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        for (TickType tickType : type)
        {
            if (tickType == TickType.CLIENT)
            {
                RelocatorHUDHandler.tick++;
                if (RelocatorHUDHandler.tick == RelocatorHUDHandler.TICKS_BETWEEN_STUFFED_ITEM_UPDATE)
                {
                    RelocatorHUDHandler.stuffedItems = null;
                }

                if (!initialized && Settings.DISPLAY_VERSION_RESULT && FMLClientHandler.instance().getClient().currentScreen == null)
                {
                    if (VersionChecker.getResult() != VersionChecker.CheckState.UNINITIALIZED)
                    {
                        initialized = true;

                        if (VersionChecker.getResult() == VersionChecker.CheckState.OUTDATED)
                        {
                            Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText(StatCollector.translateToLocalFormatted(Strings.OUTDATED, Reference.VERSION, VersionChecker.getRemoteVersion().getModVersion())));
                            Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText(StatCollector.translateToLocal(Strings.CHANGE_LOG) + ":"));
                            Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText(String.format(" %s", VersionChecker.getRemoteVersion().getChangeLog())));
                            Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText(StatCollector.translateToLocal(Strings.LATEST)));
                        }
                    }
                }
            }
        }
    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel()
    {
        return Reference.NAME + ": " + this.getClass().getSimpleName();
    }
}
