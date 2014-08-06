package com.dynious.refinedrelocation.mods;

import buildcraft.transport.ItemFacade;
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
        FMLInterModComms.sendRuntimeMessage(RefinedRelocation.instance, Mods.BC_TRANS_ID, BLACKLIST_FACADE, new ItemStack(ModBlocks.sortingConnector));

        /*
         * Re-add some facades, does not use IMC because Buildcraft thinks these will still be added (ignores blacklist)
         */
        ItemFacade.addFacade(new ItemStack(ModBlocks.sortingConnector, 1, 0));
        ItemFacade.addFacade(new ItemStack(ModBlocks.sortingConnector, 1, 1));
        ItemFacade.addFacade(new ItemStack(ModBlocks.sortingConnector, 1, 2));
    }
}
