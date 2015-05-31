package com.dynious.refinedrelocation.config;

import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.lib.Settings;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler
{
    public static Configuration configFile;

    public static void init(FMLPreInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(new ConfigHandler());
        configFile = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
    }

    public static void syncConfig()
    {
        Settings.DISABLE_WIRELESS_BLOCK_EXTENDER = getConfiguration("Disable WBE", Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT,
                "Remove crafting recipe for Wireless Block Extender.");

        Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER = getConfiguration("Max Range WBE", Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT,
                "The maximum distance the Wireless Block Extender can connect to.");

        Settings.DISPLAY_VERSION_RESULT = getConfiguration("Check Version", Settings.DISPLAY_VERSION_RESULT_DEFAULT,
                "Show the version checker result on startup.");

        Settings.DISABLE_PLAYER_RELOCATOR = getConfiguration("Disable PR", Settings.DISABLE_PLAYER_RELOCATOR_DEFAULT,
                "Remove crafting recipe for Player Relocator.");

        Settings.PLAYER_RELOCATOR_DISABLED_AGES = configFile.get(Configuration.CATEGORY_GENERAL, "Disabled Ages for PR", Settings.PLAYER_RELOCATOR_DISABLED_AGES_DEFAULT,
                "Ages from which the Player Relocator cannot teleport.").getIntList();

        Settings.PLAYER_RELOCATOR_COOLDOWN = getConfiguration("PR Cooldown Time", Settings.PLAYER_RELOCATOR_COOLDOWN_DEFAULT,
                "The Cooldown time between teleports using the Player Relocator, in seconds.");

        Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION = getConfiguration("Ticks between extractions", Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION_DEFAULT,
                "Minimum amount of time between Relocator extractions.");

        Settings.CRAFTING_MODULE_TICKS_BETWEEN_CRAFTING = getConfiguration("Ticks between crafting", Settings.CRAFTING_MODULE_TICKS_BETWEEN_CRAFTING_DEFAULT,
                "Ticks between Crafting Module craft operations.");

        Settings.DISABLE_SORTING_TO_NORMAL = getConfiguration("Disable sorting to normal", Settings.DISABLE_SORTING_TO_NORMAL_DEFAULT,
                "Disable sorting block to normal block downgrade recipe.");

        Settings.FORCE_NON_FMP_RELOCATORS = getConfiguration("Force non-FMP Relocators", Settings.FORCE_NON_FMP_RELOCATORS_DEFAULT,
                "Forces RR to use non-FMP Relocators.");

        if (configFile.hasChanged())
            configFile.save();
    }

    private static int getConfiguration(String setting, int defaultSetting, String comment)
    {
        return configFile.get(Configuration.CATEGORY_GENERAL, setting, defaultSetting, comment).getInt(defaultSetting);
    }

    private static boolean getConfiguration(String setting, boolean defaultSetting, String comment)
    {
        return configFile.get(Configuration.CATEGORY_GENERAL, setting, defaultSetting, comment).getBoolean(defaultSetting);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
    {
        if (eventArgs.modID.equals(Reference.MOD_ID))
            ConfigHandler.syncConfig();
    }
}
