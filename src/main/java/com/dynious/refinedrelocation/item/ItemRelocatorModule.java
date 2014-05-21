package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.filter.IRelocatorModule;
import com.dynious.refinedrelocation.api.item.IItemRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleFilter;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleOneWay;
import com.dynious.refinedrelocation.lib.Names;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemRelocatorModule extends Item implements IItemRelocatorModule
{
    public ItemRelocatorModule()
    {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return Names.relocatorModule + stack.getItemDamage();
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public IRelocatorModule getRelocatorFilter(ItemStack stack)
    {
        switch(stack.getItemDamage())
        {
            case 0:
                return null;
            case 1:
                return new RelocatorModuleFilter();
            case 2:
                return new RelocatorModuleOneWay();
            case 3:
                return new RelocatorModuleExtraction();
        }
        return null;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int j = 0; j < 4; ++j)
        {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }
}
