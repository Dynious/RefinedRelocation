package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.Mods;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;

public class BuildcraftIntegration
{
    private static final String BLACKLIST_FACADE = "blacklist-facade";

    public static void init()
    {
        /*
         * Blacklist Facades
         */
        FMLInterModComms.sendRuntimeMessage(RefinedRelocation.instance, Mods.BC_TRANS_ID, BLACKLIST_FACADE, new ItemStack(ModBlocks.blockExtender));
        FMLInterModComms.sendRuntimeMessage(RefinedRelocation.instance, Mods.BC_TRANS_ID, BLACKLIST_FACADE, new ItemStack(ModBlocks.relocationPortal));
    }
}
