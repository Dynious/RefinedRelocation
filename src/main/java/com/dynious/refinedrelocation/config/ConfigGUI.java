package com.dynious.refinedrelocation.config;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import com.dynious.refinedrelocation.config.ConfigHandler;
import com.dynious.refinedrelocation.lib.Reference;

public class ConfigGUI extends GuiConfig {
    public ConfigGUI(GuiScreen parent) {
        super(parent,
                new ConfigElement(ConfigHandler.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                Reference.MOD_ID, false, false, ConfigHandler.configFile.getConfigFile().toString());
    }
}