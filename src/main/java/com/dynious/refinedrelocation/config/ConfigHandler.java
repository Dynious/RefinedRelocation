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
            BlockIds.BLOCK_EXTENDER = configuration.getBlock(Names.blockExtender, BlockIds.BLOCK_EXTENDER_DEFAULT).getInt(BlockIds.BLOCK_EXTENDER_DEFAULT);
            BlockIds.BUFFER = configuration.getBlock(Names.buffer, BlockIds.BUFFER_DEFAULT).getInt(BlockIds.BUFFER_DEFAULT);
            BlockIds.SORTING_CHEST = configuration.getBlock(Names.sortingChest, BlockIds.SORTING_CHEST_DEFAULT).getInt(BlockIds.SORTING_CHEST_DEFAULT);
            BlockIds.SORTING_IRON_CHEST = configuration.getBlock(Names.sortingIronChest, BlockIds.SORTING_IRON_CHEST_DEFAULT).getInt(BlockIds.SORTING_IRON_CHEST_DEFAULT);
            BlockIds.SORTING_CONNECTOR = configuration.getBlock(Names.sortingConnector, BlockIds.SORTING_CONNECTOR_DEFAULT).getInt(BlockIds.SORTING_CONNECTOR_DEFAULT);
            BlockIds.FILTERING_HOPPER = configuration.getBlock(Names.filteringHopper, BlockIds.FILTERING_HOPPER_DEFAULT).getInt(BlockIds.FILTERING_HOPPER_DEFAULT);
            BlockIds.SORTING_BARREL = configuration.getBlock(Names.sortingBarrel, BlockIds.SORTING_BARREL_DEFAULT).getInt(BlockIds.SORTING_BARREL_DEFAULT);
            BlockIds.RELOCATION_PORTAL = configuration.getBlock(Names.relocationPortal, BlockIds.RELOCATION_PORTAL_DEFAULT).getInt(BlockIds.RELOCATION_PORTAL_DEFAULT);
            BlockIds.RELOCATION_CONTROLLER = configuration.getBlock(Names.relocationController, BlockIds.RELOCATION_CONTROLLER_DEFAULT).getInt(BlockIds.RELOCATION_CONTROLLER_DEFAULT);
            BlockIds.POWER_LIMITER = configuration.getBlock(Names.powerLimiter, BlockIds.POWER_LIMITER_DEFAULT).getInt(BlockIds.POWER_LIMITER_DEFAULT);
            BlockIds.SORTING_ALCHEMICAL_CHEST = configuration.getBlock(Names.sortingAlchemicalChest, BlockIds.SORTING_ALCHEMICAL_CHEST_DEFAULT).getInt(BlockIds.SORTING_ALCHEMICAL_CHEST_DEFAULT);
            BlockIds.RELOCATOR = configuration.getBlock(Names.relocator, BlockIds.RELOCATOR_DEFAULT).getInt(BlockIds.RELOCATOR_DEFAULT);
            BlockIds.SORTING_PRECIOUS_CHEST = configuration.getBlock(Names.sortingPreciousChest, BlockIds.SORTING_PRECIOUS_CHEST_DEFAULT).getInt(BlockIds.SORTING_PRECIOUS_CHEST_DEFAULT);
            BlockIds.LIQUID_FREEZER = configuration.getBlock(Names.liquidFreezer, BlockIds.LIQUID_FREEZER).getInt(BlockIds.LIQUID_FREEZER_DEFAULT);
            BlockIds.LIQUID_HEATER = configuration.getBlock(Names.liquidHeater, BlockIds.LIQUID_HEATER).getInt(BlockIds.LIQUID_HEATER_DEFAULT);

            ItemIds.LINKER = configuration.getItem(Names.linker, ItemIds.LINKER_DEFAULT).getInt(ItemIds.LINKER_DEFAULT);
            ItemIds.SORTING_UPGRADE = configuration.getItem(Names.sortingUpgrade, ItemIds.SORTING_UPGRADE_DEFAULT).getInt(ItemIds.SORTING_UPGRADE_DEFAULT);
            ItemIds.PLAYER_RELOCATOR = configuration.getItem(Names.playerRelocator, ItemIds.PLAYER_RELOCATOR_DEFAULT).getInt(ItemIds.PLAYER_RELOCATOR_DEFAULT);
            ItemIds.RELOCATOR_MODULE = configuration.getItem(Names.relocatorModule, ItemIds.RELOCATOR_MODULE_DEFAULT).getInt(ItemIds.RELOCATOR_MODULE_DEFAULT);
            ItemIds.TOOLBOX = configuration.getItem(Names.toolbox, ItemIds.TOOLBOX_DEFAULT).getInt(ItemIds.TOOLBOX_DEFAULT);
            ItemIds.FROZEN_LIQUID = configuration.getItem(Names.frozenLiquid, ItemIds.FROZEN_LIQUID_DEFAULT).getInt(ItemIds.FROZEN_LIQUID_DEFAULT);

            Settings.DISABLE_WIRELESS_BLOCK_EXTENDER = configuration.get(CATEGORY_SETTINGS, "Disable Wireless Block Extender", Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT).getBoolean(Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT);
            Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER = configuration.get(CATEGORY_SETTINGS, "Max Range Wireless Block Extender", Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT).getInt(Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT);
            Settings.DISPLAY_VERSION_RESULT = configuration.get(CATEGORY_SETTINGS, "Display Version Result", Settings.DISPLAY_VERSION_RESULT_DEFAULT).getBoolean(Settings.DISPLAY_VERSION_RESULT_DEFAULT);
            Settings.DISABLE_PLAYER_RELOCATOR = configuration.get(CATEGORY_SETTINGS, "Disable Player Relocator", Settings.DISABLE_PLAYER_RELOCATOR_DEFAULT).getBoolean(Settings.DISABLE_PLAYER_RELOCATOR_DEFAULT);
            Settings.PLAYER_RELOCATOR_DISABLED_AGES = configuration.get(CATEGORY_SETTINGS, "Ages Player Relocator Cannot Teleport From", Settings.PLAYER_RELOCATOR_DISABLED_AGES_DEFAULT).getIntList();
            Settings.PLAYER_RELOCATOR_COOLDOWN = configuration.get(CATEGORY_SETTINGS, "Player Relocator Cooldown Time (seconds)", Settings.PLAYER_RELOCATOR_COOLDOWN_DEFAULT).getInt(Settings.PLAYER_RELOCATOR_COOLDOWN_DEFAULT);
            Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION = configuration.get(CATEGORY_SETTINGS, "Minimum ticks between Relocator extractions", Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION_DEFAULT).getInt(Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION_DEFAULT);

        } catch (Exception ignored)
        {

        } finally
        {
            configuration.save();
        }
    }
}
