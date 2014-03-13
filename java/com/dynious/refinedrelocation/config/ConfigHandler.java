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
    private static final String CATEGORY_SETTINGS = "Settings";

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

            ItemIds.LINKER = configuration.getItem(Names.linker, ItemIds.LINKER_DEFAULT).getInt(ItemIds.LINKER_DEFAULT);
            ItemIds.SORTING_UPGRADE = configuration.getItem(Names.sortingUpgrade, ItemIds.SORTING_UPGRADE_DEFAULT).getInt(ItemIds.SORTING_UPGRADE_DEFAULT);

            Settings.DISABLE_WIRELESS_BLOCK_EXTENDER = configuration.get(CATEGORY_SETTINGS, "Disable Wireless Block Extender", Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT).getBoolean(Settings.DISABLE_WIRELESS_BLOCK_EXTENDER_DEFAULT);
            Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER = configuration.get(CATEGORY_SETTINGS, "Max Range Wireless Block Extender", Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT).getInt(Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER_DEFAULT);
        } catch (Exception ignored)
        {

        } finally
        {
            configuration.save();
        }
    }
}
