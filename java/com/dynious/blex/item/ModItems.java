package com.dynious.blex.item;

import com.dynious.blex.lib.ItemIds;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItems
{
    public static ItemLinker linker;

    public static void init()
    {
        linker = new ItemLinker(ItemIds.LINKER);

        GameRegistry.addShapedRecipe(new ItemStack(linker), "iri", "rer", "iri", 'i', Item.ingotIron, 'r', Item.redstone, 'e', Item.enderPearl);
    }
}
