package com.dynious.refinedrelocation.lib;

import com.google.common.base.Throwables;

import java.io.InputStream;
import java.util.Properties;

public class Reference
{
    static
    {
        Properties prop = new Properties();

        try
        {
            InputStream stream = Reference.class.getClassLoader().getResourceAsStream("version.properties");
            prop.load(stream);
            stream.close();
        }
        catch (Exception e)
        {
            Throwables.propagate(e); // just throw it...
        }

        VERSION = prop.getProperty("version");
    }

    public static final String MOD_ID = "RefinedRelocation";
    public static final String NAME = "Refined Relocation";
    public static final String VERSION;
    public static final String CLIENT_PROXY = "com.dynious.refinedrelocation.proxy.ClientProxy";
    public static final String COMMON_PROXY = "com.dynious.refinedrelocation.proxy.CommonProxy";
    public static final String CHANNEL_NAME = "RefRelocation";
    public static final String DEPENDENCIES = "after:IronChest;after:BuildCraft|Energy;after:IC2;after:CoFHCore;after:ComputerCraft;after:JABBA";
}
