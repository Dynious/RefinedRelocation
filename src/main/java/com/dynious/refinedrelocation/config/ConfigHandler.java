package com.dynious.refinedrelocation.config;

import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.ItemIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Settings;
import net.minecraftforge.common.Configuration;

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
            BlockIds.BLOCK_EXTENDER = getBlockID(Names.blockExtender, BlockIds.BLOCK_EXTENDER_DEFAULT, configuration);
            BlockIds.BUFFER = getBlockID(Names.buffer, BlockIds.BUFFER_DEFAULT, configuration);
            BlockIds.SORTING_CHEST = getBlockID(Names.sortingChest, BlockIds.SORTING_CHEST_DEFAULT, configuration);
            BlockIds.SORTING_IRON_CHEST = getBlockID(Names.sortingIronChest, BlockIds.SORTING_IRON_CHEST_DEFAULT, configuration);
            BlockIds.SORTING_CONNECTOR = getBlockID(Names.sortingConnector, BlockIds.SORTING_CONNECTOR_DEFAULT, configuration);
            BlockIds.FILTERING_HOPPER = getBlockID(Names.filteringHopper, BlockIds.FILTERING_HOPPER_DEFAULT, configuration);
            BlockIds.SORTING_BARREL = getBlockID(Names.sortingBarrel, BlockIds.SORTING_BARREL_DEFAULT, configuration);
            BlockIds.RELOCATION_PORTAL = getBlockID(Names.relocationPortal, BlockIds.RELOCATION_PORTAL_DEFAULT, configuration);
            BlockIds.RELOCATION_CONTROLLER = getBlockID(Names.relocationController, BlockIds.RELOCATION_CONTROLLER_DEFAULT, configuration);
            BlockIds.POWER_LIMITER = getBlockID(Names.powerLimiter, BlockIds.POWER_LIMITER_DEFAULT, configuration);
            BlockIds.SORTING_ALCHEMICAL_CHEST = getBlockID(Names.sortingAlchemicalChest, BlockIds.SORTING_ALCHEMICAL_CHEST_DEFAULT, configuration);
            BlockIds.RELOCATOR = getBlockID(Names.relocator, BlockIds.RELOCATOR_DEFAULT, configuration);
            BlockIds.SORTING_PRECIOUS_CHEST = getBlockID(Names.sortingPreciousChest, BlockIds.SORTING_PRECIOUS_CHEST_DEFAULT, configuration);

            ItemIds.LINKER = getItemID(Names.linker, ItemIds.LINKER_DEFAULT, configuration);
            ItemIds.SORTING_UPGRADE = getItemID(Names.sortingUpgrade, ItemIds.SORTING_UPGRADE_DEFAULT, configuration);
            ItemIds.PLAYER_RELOCATOR = getItemID(Names.playerRelocator, ItemIds.PLAYER_RELOCATOR_DEFAULT, configuration);
            ItemIds.RELOCATOR_MODULE = getItemID(Names.relocatorModule, ItemIds.RELOCATOR_MODULE_DEFAULT, configuration);
            ItemIds.TOOLBOX = getItemID(Names.toolbox, ItemIds.TOOLBOX_DEFAULT, configuration);

            Settings.DISABLE_WIRELESS_BLOCK_EXTENDER = getBoolean(CATEGORY_SETTINGS, "Disable Wireless Block Extender", Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT, configuration);
            Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER = getInt(CATEGORY_SETTINGS, "Max Range Wireless Block Extender", Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT, configuration);
            Settings.DISPLAY_VERSION_RESULT = getBoolean(CATEGORY_SETTINGS, "Display Version Check Result", Settings.DISPLAY_VERSION_RESULT_DEFAULT, configuration);
            Settings.DISABLE_PLAYER_RELOCATOR = getBoolean(CATEGORY_SETTINGS, "Disable Player Relocator", Settings.DISABLE_PLAYER_RELOCATOR_DEFAULT, configuration);
            Settings.PLAYER_RELOCATOR_DISABLED_AGES = getIntList(CATEGORY_SETTINGS, "Ages Player Relocator Cannot Teleport From", Settings.PLAYER_RELOCATOR_DISABLED_AGES_DEFAULT, configuration);
            Settings.PLAYER_RELOCATOR_COOLDOWN = getInt(CATEGORY_SETTINGS, "Player Relocator Cooldown Time (seconds)", Settings.PLAYER_RELOCATOR_COOLDOWN_DEFAULT, configuration);
            Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION = getInt(CATEGORY_SETTINGS, "Minimum ticks between Relocator extractions", Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION_DEFAULT, configuration);
        } catch (Exception ignored)
        {

        } finally
        {
            configuration.save();
        }
    }

    private static int getBlockID(String name, int blockDefaultID, Configuration configuration)
    {
        return configuration.getBlock(name, blockDefaultID).getInt(blockDefaultID);
    }

    private static int getItemID(String name, int itemDefaultID, Configuration configuration)
    {
        return configuration.getItem(name, itemDefaultID).getInt(itemDefaultID);
    }

    private static boolean getBoolean(String category, String description, Boolean setting, Configuration configuration)
    {
        return configuration.get(category, description, setting).getBoolean(setting);
    }

    private static int getInt(String category, String description, int setting, Configuration configuration)
    {
        return configuration.get(category, description, setting).getInt(setting);
    }

    private static int[] getIntList(String category, String description, int[] setting, Configuration configuration)
    {
        return configuration.get(category, description, setting).getIntList();
    }
}
