package com.dynious.blex.config;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.nbt.NBTTagCompound;

public class Filter
{
    public static final int STATIC_SIZE = 5;
    public boolean ingots = false;
    public boolean ores = false;
    public boolean wood = false;
    public boolean planks = false;
    public boolean dusts = false;
    public boolean[] creativeTabs = new boolean[CreativeTabs.creativeTabArray.length];

    public int getSize()
    {
        return creativeTabs.length + STATIC_SIZE;
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
            default:
                creativeTabs[place - STATIC_SIZE] = value;
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
            default:
                return creativeTabs[place - STATIC_SIZE];
        }
    }

    public String getName(int place)
    {
        switch(place)
        {
            case 0:
                return "Ingots";
            case 1:
                return "Ores";
            case 2:
                return "Wood";
            case 3:
                return "Planks";
            case 4:
                return "Dusts";
            default:
                return I18n.getString(CreativeTabs.creativeTabArray[place - STATIC_SIZE].getTranslatedTabLabel());
        }
    }
    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("ingots", ingots);
        compound.setBoolean("ores", ores);
        compound.setBoolean("wood", wood);
        compound.setBoolean("planks", planks);
        compound.setBoolean("dusts", dusts);
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
        for (int i = 0; i < creativeTabs.length; i++)
        {
            creativeTabs[i] = compound.getBoolean("creativeTabs" + i);
        }
    }
}
