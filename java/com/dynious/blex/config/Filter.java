package com.dynious.blex.config;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class Filter
{
    public boolean ingots = false;
    public boolean ores = false;
    public boolean wood = false;
    public boolean planks = false;
    public boolean dusts = false;
    public boolean[] creativeTabs = new boolean[CreativeTabs.creativeTabArray.length];
}
