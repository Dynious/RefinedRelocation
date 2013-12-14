package com.dynious.blex.tileentity;

import com.dynious.blex.config.Filter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class TileFilteredBlockExtender extends TileBlockExtender
{
    public boolean blacklist = false;
    public Filter filter = new Filter();

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack)
    {
        if (!super.isItemValidForSlot(i, itemStack))
        {
            return false;
        }
        return blacklist ? !doesItemStackPassFilter(itemStack): doesItemStackPassFilter(itemStack);
    }

    private boolean doesItemStackPassFilter(ItemStack itemStack)
    {
        String oreName = OreDictionary.getOreName(OreDictionary.getOreID(itemStack));
        if (filter.ingots && oreName.toLowerCase().contains("ingot"))
            return true;
        if (filter.ores && oreName.toLowerCase().contains("ore"))
            return true;
        if(filter.wood && oreName.toLowerCase().contains("wood"))
            return true;
        if (filter.planks && oreName.toLowerCase().contains("plank"))
            return true;
        if (filter.dusts && (oreName.toLowerCase().contains("dust") || oreName.toLowerCase().contains("crushed")))
            return true;

        int index = itemStack.getItem().getCreativeTab().getTabIndex();
        for (int i = 0; i < filter.creativeTabs.length; i++)
        {
            if (filter.creativeTabs[i] && index == i)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("blacklist", blacklist);
        filter.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        blacklist = compound.getBoolean("blacklist");
        filter.readFromNBT(compound);
    }
}
