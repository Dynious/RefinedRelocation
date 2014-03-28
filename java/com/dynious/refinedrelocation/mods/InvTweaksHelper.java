package com.dynious.refinedrelocation.mods;

import cpw.mods.fml.common.Mod;
import invtweaks.api.InvTweaksAPI;

public class InvTweaksHelper
{
    @Mod.Instance("inventorytweaks")
    private static InvTweaksAPI invTweaksInstance;

    public static void disableInvTweaks()
    {
        invTweaksInstance.setTextboxMode(true);
    }
}
