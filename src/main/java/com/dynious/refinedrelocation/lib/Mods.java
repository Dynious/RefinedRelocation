package com.dynious.refinedrelocation.lib;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;

public class Mods {
    public static final String IRON_CHEST_ID = "IronChest";
    public static boolean IS_IRON_CHEST_LOADED;
    public static final String JABBA_ID = "JABBA";
    public static boolean IS_JABBA_LOADED;
    public static final String EE3_ID = "EE3";
    public static boolean IS_EE3_LOADED;
    public static final String BC_TRANS_API_ID = "BuildCraftAPI|transport";
    public static boolean IS_BC_TRANSPORT_API_LOADED;
    public static final String BC_TRANS_ID = "BuildCraft|Transport";
    public static final String COFH_TRANSPORT_API_ID = "CoFHAPI|transport";
    public static boolean IS_COFH_TRANSPORT_API_LOADED;
    public static final String COFH_ENERGY_API_ID = "CoFHAPI|energy";
    public static boolean IS_COFH_ENERGY_API_LOADED;
    public static final String COFH_BLOCK_API_ID = "CoFHAPI|block";
    public static final String IC2_ID = "IC2";
    public static boolean IS_IC2_LOADED;
    public static final String FMP_ID = "McMultipart";
    public static boolean IS_FMP_LOADED;
    public static final String WAILA_ID = "Waila";
    public static boolean IS_WAILA_LOADED;
    public static final String AE2_ID = "appliedenergistics2";
    public static boolean IS_AE2_LOADED;

    public static void init() {
        IS_IRON_CHEST_LOADED = Loader.isModLoaded(IRON_CHEST_ID);
        IS_JABBA_LOADED = Loader.isModLoaded(JABBA_ID);
        IS_EE3_LOADED = Loader.isModLoaded(EE3_ID);
        IS_BC_TRANSPORT_API_LOADED = ModAPIManager.INSTANCE.hasAPI(BC_TRANS_API_ID);
        IS_COFH_TRANSPORT_API_LOADED = ModAPIManager.INSTANCE.hasAPI(COFH_TRANSPORT_API_ID);
        IS_COFH_ENERGY_API_LOADED = ModAPIManager.INSTANCE.hasAPI(COFH_ENERGY_API_ID);
        IS_IC2_LOADED = Loader.isModLoaded(IC2_ID);
        IS_FMP_LOADED = !Settings.FORCE_NON_FMP_RELOCATORS && Loader.isModLoaded(FMP_ID);
        IS_WAILA_LOADED = Loader.isModLoaded(WAILA_ID);
        IS_AE2_LOADED = Loader.isModLoaded(AE2_ID);
    }
}
