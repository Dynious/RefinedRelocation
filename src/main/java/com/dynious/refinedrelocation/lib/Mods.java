package com.dynious.refinedrelocation.lib;

import cpw.mods.fml.common.Loader;

public class Mods
{
    public static final boolean IS_IRON_CHEST_LOADED = Loader.isModLoaded("IronChest");
    public static final boolean IS_JABBA_LOADED = Loader.isModLoaded("JABBA");
    public static final boolean IS_EE3_LOADED = Loader.isModLoaded("EE3");
    public static final boolean IS_BC_TRANS_LOADED = Loader.isModLoaded("BuildCraft|Transport");
    public static final boolean IS_BC_ENERGY_LOADED = Loader.isModLoaded("BuildCraft|Energy");
    public static final boolean IS_COFH_CORE_LOADED = Loader.isModLoaded("CoFHCore");
    public static final boolean IS_IC2_LOADED = Loader.isModLoaded("IC2");
    public static final boolean IS_UE_LOADED = Loader.isModLoaded("UniversalElectricity");
    public static final boolean IS_FMP_LOADED = Loader.isModLoaded("McMultipart");
    public static final boolean IS_METAL_LOADED = Loader.isModLoaded("Metallurgy3Machines");
}
