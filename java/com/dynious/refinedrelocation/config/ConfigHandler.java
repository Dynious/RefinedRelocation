package com.dynious.refinedrelocation.config;

import com.dynious.refinedrelocation.lib.Settings;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler
{
    public static Configuration configuration;
    public static final String CATEGORY_SETTINGS = "Settings";

    public static void init(File configFile)
    {
        configuration = new Configuration(configFile);
        try
        {
            Settings.DISABLE_WIRELESS_BLOCK_EXTENDER = configuration.get(CATEGORY_SETTINGS, "Disable Wireless Block Extender", Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT).getBoolean(Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT);
            Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER = configuration.get(CATEGORY_SETTINGS, "Max Range Wireless Block Extender", Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT).getInt(Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT);
            Settings.DISPLAY_VERSION_RESULT = configuration.get(CATEGORY_SETTINGS, "Display Version Result", Settings.DISPLAY_VERSION_RESULT_DEFAULT).getBoolean(Settings.DISPLAY_VERSION_RESULT_DEFAULT);

        } catch (Exception ignored)
        {

        } finally
        {
            configuration.save();
        }
    }
}
