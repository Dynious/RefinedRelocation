package com.dynious.refinedrelocation.config;

import com.dynious.refinedrelocation.lib.Settings;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
    public static Configuration configFile;

    public static void init(FMLPreInitializationEvent event)
    {
        configFile = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
    }

    public static void syncConfig()
    {
        Settings.DISABLE_WIRELESS_BLOCK_EXTENDER = configFile.get(Configuration.CATEGORY_GENERAL, "Disable WBE", Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT,
                "Remove crafting recipe for Wireless Block Extender.").getBoolean(Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT);

        Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER = configFile.get(Configuration.CATEGORY_GENERAL, "Max Range WBE", Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT,
                "The maximum distance the Wireless Block Extender can connect to.").getInt(Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT);

        Settings.DISPLAY_VERSION_RESULT = configFile.get(Configuration.CATEGORY_GENERAL, "Check Version", Settings.DISPLAY_VERSION_RESULT_DEFAULT,
                "Show the version checker result on startup.").getBoolean(Settings.DISPLAY_VERSION_RESULT_DEFAULT);

        Settings.DISABLE_PLAYER_RELOCATOR = configFile.get(Configuration.CATEGORY_GENERAL, "Disable PR", Settings.DISABLE_PLAYER_RELOCATOR_DEFAULT,
                "Remove crafting recipe for Player Relocator.").getBoolean(Settings.DISABLE_PLAYER_RELOCATOR_DEFAULT);

        Settings.PLAYER_RELOCATOR_DISABLED_AGES = configFile.get(Configuration.CATEGORY_GENERAL, "Disabled Ages for PR", Settings.PLAYER_RELOCATOR_DISABLED_AGES_DEFAULT,
                "Ages from which the Player Relocator cannot teleport.").getIntList();

        Settings.PLAYER_RELOCATOR_COOLDOWN = configFile.get(Configuration.CATEGORY_GENERAL, "PR Cooldown Time", Settings.PLAYER_RELOCATOR_COOLDOWN_DEFAULT,
                "The Cooldown time between teleports using the Player Relocator, in seconds.").getInt(Settings.PLAYER_RELOCATOR_COOLDOWN_DEFAULT);

        Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION = configFile.get(Configuration.CATEGORY_GENERAL, "Ticks between extractions", Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION_DEFAULT,
                "Minimum amount of time between Relocator extractions.").getInt(Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION_DEFAULT);

        if (configFile.hasChanged())
            configFile.save();
    }
}
