package com.dynious.blex.config;

import com.dynious.blex.lib.BlockIds;
import com.dynious.blex.lib.ItemIds;
import com.dynious.blex.lib.Names;
import com.dynious.blex.lib.Settings;
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
            BlockIds.FILTERING_CHEST = configuration.getBlock(Names.filteringChest, BlockIds.FILTERING_CHEST_DEFAULT).getInt(BlockIds.FILTERING_CHEST_DEFAULT);
            BlockIds.FILTERING_IRON_CHEST = configuration.getBlock(Names.filteringIronChest, BlockIds.FILTERING_IRON_CHEST_DEFAULT).getInt(BlockIds.FILTERING_IRON_CHEST_DEFAULT);

            ItemIds.LINKER = configuration.getItem(Names.linker, ItemIds.LINKER_DEFAULT).getInt(ItemIds.LINKER_DEFAULT);
            ItemIds.FILTERING_UPGRADE = configuration.getItem(Names.filteringUpgrade, ItemIds.FILTERING_UPGRADE_DEFAULT).getInt(ItemIds.FILTERING_UPGRADE_DEFAULT);

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
