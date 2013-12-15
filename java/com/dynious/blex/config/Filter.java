package com.dynious.blex.config;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class Filter
{
    public static final int STATIC_SIZE = 7;
    public boolean ingots = false;
    public boolean ores = false;
    public boolean wood = false;
    public boolean planks = false;
    public boolean dusts = false;
    public boolean crushedOres = false;
    public boolean purifiedCrushedOres = false;
    public boolean[] creativeTabs = new boolean[CreativeTabs.creativeTabArray.length];

    public int getSize()
    {
        return creativeTabs.length - 2 + STATIC_SIZE;
    }

    public boolean passesFilter(ItemStack itemStack)
    {
        String oreName = OreDictionary.getOreName(OreDictionary.getOreID(itemStack));
        if (ingots && oreName.toLowerCase().contains("ingot"))
            return true;
        if (ores && oreName.toLowerCase().contains("ore"))
            return true;
        if(wood && oreName.toLowerCase().contains("wood"))
            return true;
        if (planks && oreName.toLowerCase().contains("plank"))
            return true;
        if (dusts && oreName.toLowerCase().contains("dust"))
            return true;
        if (crushedOres && oreName.toLowerCase().contains("crushed") && !oreName.toLowerCase().contains("purified"))
            return true;
        if (purifiedCrushedOres && !oreName.toLowerCase().contains("purified"))
            return true;

        int index = itemStack.getItem().getCreativeTab().getTabIndex();
        for (int i = 0; i < creativeTabs.length; i++)
        {
            if (creativeTabs[i] && index == i)
            {
                return true;
            }
        }
        return false;
    }

    public void setValue(int place, boolean value)
    {
        switch(place)
        {
            case 0:
                ingots = value;
                break;
            case 1:
                ores = value;
                break;
            case 2:
                wood = value;
                break;
            case 3:
                planks = value;
                break;
            case 4:
                dusts = value;
                break;
            case 5:
                crushedOres = value;
                break;
            case 6:
                purifiedCrushedOres = value;
                break;
            default:
                creativeTabs[getCreativeTab(place)] = value;
                break;
        }
    }

    public boolean getValue(int place)
    {
        switch(place)
        {
            case 0:
                return ingots;
            case 1:
                return ores;
            case 2:
                return wood;
            case 3:
                return planks;
            case 4:
                return dusts;
            case 5:
                return crushedOres;
            case 6:
                return purifiedCrushedOres;
            default:
                return creativeTabs[getCreativeTab(place)];
        }
    }

    public String getName(int place)
    {
        switch(place)
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
            default:
                return I18n.getString(CreativeTabs.creativeTabArray[getCreativeTab(place)].getTranslatedTabLabel());
        }
    }

    public int getCreativeTab(int place)
    {
        int index = place - STATIC_SIZE;
        if (index >= 5)
            index++;
        if (index >= 11)
            index++;
        return index;
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("ingots", ingots);
        compound.setBoolean("ores", ores);
        compound.setBoolean("wood", wood);
        compound.setBoolean("planks", planks);
        compound.setBoolean("dusts", dusts);
        compound.setBoolean("crushedOres", crushedOres);
        compound.setBoolean("purifiedCrushedOres", purifiedCrushedOres);
        for (int i = 0; i < creativeTabs.length; i++)
        {
            compound.setBoolean("creativeTabs" + i, creativeTabs[i]);
        }
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        ingots = compound.getBoolean("ingots");
        ores = compound.getBoolean("ores");
        wood = compound.getBoolean("wood");
        planks = compound.getBoolean("planks");
        dusts = compound.getBoolean("dusts");
        crushedOres = compound.getBoolean("crushedOres");
        purifiedCrushedOres = compound.getBoolean("purifiedCrushedOres");
        for (int i = 0; i < creativeTabs.length; i++)
        {
            creativeTabs[i] = compound.getBoolean("creativeTabs" + i);
        }
    }
}
