package com.dynious.refinedrelocation.lib;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;

public class Mods
{
    public static final String IRON_CHEST_ID = "IronChest";
    public static final String JABBA_ID = "JABBA";
    public static final String EE3_ID = "EE3";
    public static final String BC_API_TRANS_ID = "BuildCraftAPI|transport";
    public static final String BC_API_POWER_ID = "BuildCraftAPI|power";
    public static final String COFH_API_ID = "CoFHAPI";
    public static final String IC2_ID = "IC2";
    public static final String FMP_IS = "McMultipart";
    public static final String WAILA_ID = "Waila";


    public static final boolean IS_IRON_CHEST_LOADED = Loader.isModLoaded(IRON_CHEST_ID);
    public static final boolean IS_JABBA_LOADED = Loader.isModLoaded(JABBA_ID);
    public static final boolean IS_EE3_LOADED = Loader.isModLoaded(EE3_ID);
    public static final boolean IS_BC_TRANS_LOADED = ModAPIManager.INSTANCE.hasAPI(BC_API_TRANS_ID);
    public static final boolean IS_BC_ENERGY_LOADED = ModAPIManager.INSTANCE.hasAPI(BC_API_POWER_ID);
    public static final boolean IS_COFH_API_LOADED =ModAPIManager.INSTANCE.hasAPI(COFH_API_ID);
    public static final boolean IS_IC2_LOADED = Loader.isModLoaded(IC2_ID);
    public static final boolean IS_FMP_LOADED = Loader.isModLoaded(FMP_IS);
    public static final boolean IS_WAILA_LOADED = Loader.isModLoaded(WAILA_ID);
}
