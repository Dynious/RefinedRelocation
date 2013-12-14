package com.dynious.blex.config;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Filter
{
    public boolean ingots = false;
    public boolean ores = false;
    public boolean wood = false;
    public boolean planks = false;
    public boolean dusts = false;
    public boolean[] creativeTabs = new boolean[CreativeTabs.creativeTabArray.length];

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
