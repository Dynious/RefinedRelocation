package com.dynious.refinedrelocation.helper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackHelper
{
    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2)
    {
        return itemStack1 == null && itemStack2 == null || (!(itemStack1 == null || itemStack2 == null) && (itemStack1.getItem() == itemStack2.getItem() && (itemStack1.getItemDamage() == itemStack2.getItemDamage() && (!(itemStack1.stackTagCompound == null && itemStack2.stackTagCompound != null) && (itemStack1.stackTagCompound == null || itemStack1.stackTagCompound.equals(itemStack2.stackTagCompound))))));
    }

    public static boolean areOreDictEntriesSame(ItemStack itemStack1, ItemStack itemStack2)
    {
        int[] a = OreDictionary.getOreIDs(itemStack1);
        if (a.length != 0)
        {
            int[] b = OreDictionary.getOreIDs(itemStack2);
            for (int bb : b)
            {
                for (int aa : a)
                {
                    if (aa == bb)
                        return true;
                }
            }
        }
        return false;
    }
}
