package com.dynious.blex.config;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;

public class Filter
{
    public static final int FILTER_SIZE = 9;
    public boolean[] customFilters = new boolean[FILTER_SIZE];
    public boolean[] creativeTabs = new boolean[CreativeTabs.creativeTabArray.length];
    public String userFilter = "";
    private Field tabField;

    public Filter()
    {
        try
        {
            tabField = Item.class.getDeclaredField("tabToDisplayOn");
            tabField.setAccessible(true);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }
    public int getSize()
    {
        return creativeTabs.length - 2 + FILTER_SIZE;
    }

    public boolean passesFilter(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            String oreName = OreDictionary.getOreName(OreDictionary.getOreID(itemStack)).toLowerCase();
            String filter = userFilter.toLowerCase().replaceAll("\\s+","");
            for (String s : filter.split(","))
            {
                String filterName = "";
                if (s.contains("!"))
                {
                    filterName = oreName;
                    s = s.replace("!", "");
                }
                else
                {
                    filterName = itemStack.getDisplayName().toLowerCase();
                }
                if (s.startsWith("*") && s.length() > 1)
                {
                    if (s.endsWith("*") && s.length() > 2)
                    {
                        if (filterName.contains(s.substring(1, s.length()-1)))
                            return true;
                    }
                    else if (filterName.endsWith(s.substring(1)))
                        return true;
                }
                else if (s.endsWith("*") && s.length() > 1)
                {
                    if (filterName.startsWith(s.substring(0, s.length()-1)))
                        return true;
                }
                else
                {
                    if (filterName.equalsIgnoreCase(s))
                        return true;
                }
            }

            if (customFilters[0] && oreName.contains("ingot"))
                return true;
            if (customFilters[1] && oreName.contains("ore"))
                return true;
            if (customFilters[2] && oreName.contains("wood"))
                return true;
            if (customFilters[3] && oreName.contains("plank"))
                return true;
            if (customFilters[4] && oreName.contains("dust"))
                return true;
            if (customFilters[5] && oreName.contains("crushed") && !oreName.toLowerCase().contains("purified"))
                return true;
            if (customFilters[6] && !oreName.contains("purified"))
                return true;
            if (customFilters[7] && !oreName.contains("plate"))
                return true;
            if (customFilters[8] && !oreName.contains("gem"))
                return true;

            if (itemStack.getItem() != null)
            {
                CreativeTabs tab = null;
                try
                {
                    tab = (CreativeTabs)tabField.get(itemStack.getItem());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                if (tab != null)
                {
                    int index = tab.getTabIndex();
                    for (int i = 0; i < creativeTabs.length; i++)
                    {
                        if (creativeTabs[i] && index == i)
                        {
                            return true;
                        }
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
        compound.setString("userFilter", userFilter);
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
        userFilter = compound.getString("userFilter");
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
