package com.dynious.refinedrelocation.config;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.lib.Settings;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
    public static Configuration configFile;

    public static void init(FMLPreInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(RefinedRelocation.instance);
        configFile = new Configuration(event.getSuggestedConfigurationFile());
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equals(Reference.MOD_ID))
            syncConfig();
    }

    public static void syncConfig()
    {
        Settings.DISABLE_WIRELESS_BLOCK_EXTENDER = configFile.get(Configuration.CATEGORY_GENERAL, "Disable WBE", Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT,
                "Remove crafting recipe for Wireless Block Extender.").getBoolean();
        Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER = configFile.get(Configuration.CATEGORY_GENERAL, "Max Range WBE", Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT,
                "The maximum distance a block can be from the wireless block extender.").getInt();
        Settings.DISPLAY_VERSION_RESULT = configFile.get(Configuration.CATEGORY_GENERAL, "Check Version", Settings.DISPLAY_VERSION_RESULT_DEFAULT,
                "If Refined Relocation should check if the mod is updated on startup.").getBoolean();
        Settings.DISABLE_PLAYER_RELOCATOR = configFile.get(Configuration.CATEGORY_GENERAL, "Disable PR", Settings.DISABLE_PLAYER_RELOCATOR_DEFAULT,
                "Remove crafting recipe for Player Relocator.").getBoolean();
        Settings.PLAYER_RELOCATOR_DISABLED_AGES = configFile.get(Configuration.CATEGORY_GENERAL, "Disabled Ages for PR", Settings.PLAYER_RELOCATOR_DISABLED_AGES_DEFAULT,
                "Ages from which the Player Relocator cannot teleport.").getIntList();
        Settings.PLAYER_RELOCATOR_COOLDOWN = configFile.get(Configuration.CATEGORY_GENERAL, "PR Cooldown Time", Settings.PLAYER_RELOCATOR_COOLDOWN_DEFAULT,
                "The Cooldown time between teleports using the Player Relocator, in seconds.").getInt();
        Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION = configFile.get(Configuration.CATEGORY_GENERAL, "Ticks between extractions", Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION_DEFAULT,
                "Minimum amount of time between Relocator extractions.").getInt();

        if (configFile.hasChanged())
            configFile.save();
    }
}