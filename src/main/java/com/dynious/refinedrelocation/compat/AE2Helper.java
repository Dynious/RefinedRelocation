package com.dynious.refinedrelocation.compat;

import appeng.api.AEApi;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.google.common.base.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AE2Helper {
    public static void addAERecipes() {
        Optional<ItemStack> certusQuartzCrystal = AEApi.instance().definitions().materials().certusQuartzCrystal().maybeStack(1);
        Optional<ItemStack> iface = AEApi.instance().definitions().blocks().iface().maybeStack(1);
        if (certusQuartzCrystal.isPresent() && iface.isPresent()) {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.sortingConnector, 1, 3), "gcg", "cic", "gcg", 'g', Items.gold_ingot, 'c', certusQuartzCrystal.get(), 'i', iface.get());
        }
    }
}
