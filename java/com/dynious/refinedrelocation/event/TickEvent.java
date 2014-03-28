package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.version.VersionChecker;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatMessageComponent;

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
        if (Settings.DISPLAY_VERSION_RESULT)
        {
            if (!initialized)
            {
                for (TickType tickType : type)
                {
                    if (tickType == TickType.CLIENT)
                    {
                        if (FMLClientHandler.instance().getClient().currentScreen == null)
                        {
                            if (VersionChecker.getResult() != VersionChecker.CheckState.UNINITIALIZED)
                            {
                                initialized = true;

                                if (VersionChecker.getResult() == VersionChecker.CheckState.OUTDATED)
                                {
                                    Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("Refined Relocation %s is outdated! Latest version: %s", Reference.VERSION, VersionChecker.getRemoteVersion().getModVersion())));
                                    Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText("Change log:"));
                                    Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText(String.format(" %s", VersionChecker.getRemoteVersion().getChangeLog())));
                                    Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText("Get latest using: '/RefinedRelocation latest'"));
                                }
                            }
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
