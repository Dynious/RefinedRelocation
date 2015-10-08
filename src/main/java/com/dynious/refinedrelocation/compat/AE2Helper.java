package com.dynious.refinedrelocation.compat;

import appeng.api.AEApi;
import com.dynious.refinedrelocation.block.ModBlocks;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AE2Helper
{
    public static void addAERecipes()
    {
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.sortingConnector, 1, 3), "gcg", "cic", "gcg", 'g', Items.gold_ingot, 'c', AEApi.instance().materials().materialCertusQuartzCrystal.stack(1), 'i', AEApi.instance().blocks().blockInterface.stack(1));
    }
}
