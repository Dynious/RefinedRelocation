package com.dynious.refinedrelocation.item;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeToolbox implements IRecipe
{
    private ItemStack resultStack;

    @Override
    public boolean matches(InventoryCrafting inventorycrafting, World world)
    {
        for (int slot = 0; slot < inventorycrafting.getSizeInventory(); slot++)
        {
            ItemStack stack = inventorycrafting.getStackInSlot(slot);
            if (stack != null && stack.getItem() instanceof ItemToolBox)
            {
                ItemStack wrench = ItemToolBox.getCurrentWrench(stack);
                if (wrench != null)
                {
                    resultStack = wrench;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting)
    {
        return resultStack;
    }

    @Override
    public int getRecipeSize()
    {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return resultStack;
    }
}
