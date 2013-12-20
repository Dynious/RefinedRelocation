package com.dynious.blex.config;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class Filter
{
    public static final int FILTER_SIZE = 9;
    public boolean[] customFilters = new boolean[FILTER_SIZE];
    public boolean[] creativeTabs = new boolean[CreativeTabs.creativeTabArray.length];

    public int getSize()
    {
        return creativeTabs.length - 2 + FILTER_SIZE;
    }

    public boolean passesFilter(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            String oreName = OreDictionary.getOreName(OreDictionary.getOreID(itemStack));

            if (customFilters[0] && oreName.toLowerCase().contains("ingot"))
                return true;
            if (customFilters[1] && oreName.toLowerCase().contains("ore"))
                return true;
            if (customFilters[2] && oreName.toLowerCase().contains("wood"))
                return true;
            if (customFilters[3] && oreName.toLowerCase().contains("plank"))
                return true;
            if (customFilters[4] && oreName.toLowerCase().contains("dust"))
                return true;
            if (customFilters[5] && oreName.toLowerCase().contains("crushed") && !oreName.toLowerCase().contains("purified"))
                return true;
            if (customFilters[6] && !oreName.toLowerCase().contains("purified"))
                return true;
            if (customFilters[7] && !oreName.toLowerCase().contains("plate"))
                return true;
            if (customFilters[8] && !oreName.toLowerCase().contains("gem"))
                return true;

            if (itemStack.getItem() != null && itemStack.getItem().getCreativeTab() != null)
            {
                int index = itemStack.getItem().getCreativeTab().getTabIndex();
                for (int i = 0; i < creativeTabs.length; i++)
                {
                    if (creativeTabs[i] && index == i)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setValue(int place, boolean value)
    {
        if (place < customFilters.length)
        {
            customFilters[place] = value;
        }
        else
        {
            creativeTabs[getCreativeTab(place)] = value;
        }
    }

    public boolean getValue(int place)
    {
        if (place < customFilters.length)
        {
            return customFilters[place];
        }
        else
        {
            return creativeTabs[getCreativeTab(place)];
        }
    }

    public String getName(int place)
    {
        switch (place)
        {
            case 0:
                return "All Ingots";
            case 1:
                return "All Ores";
            case 2:
                return "All Woods";
            case 3:
                return "All Planks";
            case 4:
                return "All Dusts";
            case 5:
                return "All Crushed Ores";
            case 6:
                return "All Purified Ores";
            case 7:
                return "All Plates";
            case 8:
                return "All Gems";
            default:
                return I18n.getString(CreativeTabs.creativeTabArray[getCreativeTab(place)].getTranslatedTabLabel());
        }
    }

    public int getCreativeTab(int place)
    {
        int index = place - FILTER_SIZE;
        if (index >= 5)
            index++;
        if (index >= 11)
            index++;
        return index;
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        for (int i = 0; i < customFilters.length; i++)
        {
            compound.setBoolean("cumstomFilters" + i, customFilters[i]);
        }
        for (int i = 0; i < creativeTabs.length; i++)
        {
            compound.setBoolean("creativeTabs" + i, creativeTabs[i]);
        }
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        for (int i = 0; i < customFilters.length; i++)
        {
            customFilters[i] = compound.getBoolean("cumstomFilters" + i);
        }
        for (int i = 0; i < creativeTabs.length; i++)
        {
            creativeTabs[i] = compound.getBoolean("creativeTabs" + i);
        }
    }
}
