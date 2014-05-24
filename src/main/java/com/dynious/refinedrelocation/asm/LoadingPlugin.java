package com.dynious.refinedrelocation.asm;

import codechicken.core.launch.DepLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"com.dynious.refinedrelocation.asm"})
public class LoadingPlugin implements IFMLLoadingPlugin
{
    public LoadingPlugin()
    {
        DepLoader.load();
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }
}
