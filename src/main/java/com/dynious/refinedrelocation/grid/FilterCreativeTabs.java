package com.dynious.refinedrelocation.grid;

import com.dynious.refinedrelocation.api.filter.IFilterModule;
import com.dynious.refinedrelocation.client.gui.IGuiWidgetBase;
import com.google.common.primitives.Booleans;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FilterCreativeTabs implements IFilterModule
{
    private static CreativeTabs[] tabs = CreativeTabs.creativeTabArray;
    private boolean[] creativeTabs = new boolean[tabs.length];

    public void setValue(int place, boolean value)
    {
        creativeTabs[place] = value;
    }

    public boolean getValue(int place)
    {
        return creativeTabs[place];
    }

    public String getName(int place)
    {
        return I18n.format(tabs[place].getTranslatedTabLabel()).replace("itemGroup.", "");
    }

    @Override
    public String getDisplayName()
    {
        return null;
    }

    @Override
    public boolean matchesFilter(ItemStack stack)
    {
        if (Booleans.contains(creativeTabs, true))
        {
            CreativeTabs tab;
            if (stack.getItem() instanceof ItemBlock)
            {
                tab = Block.getBlockById(ItemBlock.getIdFromItem(stack.getItem())).displayOnCreativeTab;
            }
            else
            {
                tab = stack.getItem().tabToDisplayOn;
            }
            if (tab != null)
            {
                int index = tab.tabIndex;

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

    @Override
    public IGuiWidgetBase getGUI()
    {
        return null;
    }

    @Override
    public Container getContainer()
    {
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        String filters = compound.getString("filters");
        for (String string : filters.split("\\^\\$"))
        {
            for (int i = 0; i < tabs.length && i < creativeTabs.length; i++)
            {
                if (string.equals(tabs[i].tabLabel))
                    creativeTabs[i] = true;
            }
        }

        //1.0.7- compat
        for (int i = 0; compound.hasKey("creativeTabs" + i) && i < creativeTabs.length; i++)
        {
            creativeTabs[i] = compound.getBoolean("creativeTabs" + i);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tabs.length && i < creativeTabs.length; i++)
        {
            if (creativeTabs[i])
            {
                builder.append(tabs[i].tabLabel).append("^$");
            }
        }
        compound.setString("filters", builder.toString());
    }

    public static void syncTabs(String[] tabLabels)
    {
        tabs = new CreativeTabs[tabLabels.length];

        for (int i = 0; i < tabLabels.length; i++)
        {
            String label = tabLabels[i];
            if (label != null)
            {
                for (CreativeTabs tab : CreativeTabs.creativeTabArray)
                {
                    if (label.equalsIgnoreCase(tab.getTabLabel()))
                    {
                        tabs[i] = tab;
                    }
                }
            }
            if (tabs[i] == null)
                tabs[i] = createNewFakeTab(label);
        }
    }

    public static CreativeTabs createNewFakeTab(String tabName)
    {
        CreativeTabs oldTab = CreativeTabs.creativeTabArray[0];
        CreativeTabs tab = new CreativeTabs(0, tabName)
        {
            @Override
            public Item getTabIconItem()
            {
                return null;
            }
        };
        CreativeTabs.creativeTabArray[0] = oldTab;
        return tab;
    }
}
