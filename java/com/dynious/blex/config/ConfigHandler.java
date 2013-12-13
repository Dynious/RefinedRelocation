package com.dynious.blex.config;

import com.dynious.blex.lib.BlockIds;
import com.dynious.blex.lib.Names;
import net.minecraftforge.common.Configuration;

import java.io.File;

public class ConfigHandler
{
    public static Configuration configuration;

    public static void init(File configFile)
    {
        configuration = new Configuration(configFile);
        try
        {
            BlockIds.BLOCK_EXTENDER = configuration.getBlock(Names.blockExtender, BlockIds.BLOCK_EXTENDER_DEFAULT).getInt(BlockIds.BLOCK_EXTENDER_DEFAULT);
        } catch (Exception e)
        {

        } finally
        {
            configuration.save();
        }
    }
}
