package com.dynious.refinedrelocation.grid;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.lib.Strings;
import com.google.common.primitives.Booleans;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;

public class FilterStandard implements IFilterGUI
{
    private static Field displayOnCreativeTab = ReflectionHelper.findField(Block.class, ObfuscationReflectionHelper.remapFieldNames(Block.class.getName(), "displayOnCreativeTab", "field_149772_a", "a"));
    private static Field tabToDisplayOn = ReflectionHelper.findField(Item.class, ObfuscationReflectionHelper.remapFieldNames(Item.class.getName(), "tabToDisplayOn", "field_77701_a", "a"));
    private static Field tabIndex = ReflectionHelper.findField(CreativeTabs.class, ObfuscationReflectionHelper.remapFieldNames(CreativeTabs.class.getName(), "tabIndex", "field_78033_n", "n"));
    private static Field tabLabel = ReflectionHelper.findField(CreativeTabs.class, ObfuscationReflectionHelper.remapFieldNames(CreativeTabs.class.getName(), "tabLabel", "field_78034_o", "o"));

    private static CreativeTabs[] tabs = CreativeTabs.creativeTabArray;

    public static final int FILTER_SIZE = 14;

    private IFilterTileGUI tile;

    private boolean[] customFilters = new boolean[FILTER_SIZE];
    private boolean[] creativeTabs = new boolean[tabs.length];
    private String userFilter = "";

    private boolean blacklists = false;

    public FilterStandard(IFilterTileGUI tile)
    {
        this.tile = tile;
    }

    public int getSize()
    {
        return creativeTabs.length - 2 + FILTER_SIZE;
    }

    public boolean passesFilter(ItemStack itemStack)
    {
        return isBlacklisting() ? !isInFilter(itemStack) : isInFilter(itemStack);
    }

    private boolean isInFilter(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            String[] oreNames = null;

            if (getUserFilter() != null && !getUserFilter().isEmpty())
            {
                String filter = getUserFilter().toLowerCase().replaceAll("\\s+", "");
                String itemName = null;
                for (String s : filter.split(","))
                {
                    if (s.contains("!"))
                    {
                        if (oreNames == null)
                        {
                            oreNames = getOreNames(itemStack);
                        }
                        s = s.replace("!", "");
                        for (String oreName : oreNames)
                        {
                            if (stringMatchesWildcardPattern(oreName, s))
                                return true;
                        }
                    }
                    else
                    {
                        if (itemName == null)
                        {
                            itemName = itemStack.getDisplayName().toLowerCase().replaceAll("\\s+", "");
                        }
                        if (stringMatchesWildcardPattern(itemName, s))
                            return true;
                    }
                }
            }

            if (Booleans.contains(customFilters, true))
            {
                if (oreNames == null)
                {
                    oreNames = getOreNames(itemStack);
                }

                for (String oreName : oreNames)
                {
                    if (customFilters[0] && (oreName.contains("ingot") || itemStack.getItem() == Items.iron_ingot || itemStack.getItem() == Items.gold_ingot))
                        return true;
                    if (customFilters[1] && oreName.contains("ore"))
                        return true;
                    if (customFilters[2] && oreName.contains("log"))
                        return true;
                    if (customFilters[3] && oreName.contains("plank"))
                        return true;
                    if (customFilters[4] && oreName.contains("dust"))
                        return true;
                    if (customFilters[5] && oreName.contains("crushed") && !oreName.contains("purified"))
                        return true;
                    if (customFilters[6] && oreName.contains("purified"))
                        return true;
                    if (customFilters[7] && oreName.contains("plate"))
                        return true;
                    if (customFilters[8] && oreName.contains("gem"))
                        return true;
                    if (customFilters[10] && oreName.contains("dye"))
                        return true;
                    if (customFilters[11] && oreName.contains("nugget"))
                        return true;
                }
                if (customFilters[9] && itemStack.getItem() instanceof ItemFood)
                    return true;
                if (customFilters[12] && itemStack.getItem() instanceof ItemBlock && Block.getBlockFromItem(itemStack.getItem()) instanceof IPlantable)
                    return true;
                if (customFilters[13] && TileEntityFurnace.getItemBurnTime(itemStack) > 0)
                    return true;
            }

            if (Booleans.contains(creativeTabs, true))
            {
                try
                {
                    CreativeTabs tab;

                    if (itemStack.getItem() instanceof ItemBlock)
                    {
                        tab = (CreativeTabs) displayOnCreativeTab.get(Block.getBlockById(ItemBlock.getIdFromItem(itemStack.getItem())));
                    }
                    else
                    {
                        tab = (CreativeTabs) tabToDisplayOn.get(itemStack.getItem());
                    }
                    if (tab != null)
                    {
                        int index = tabIndex.getInt(tab);

                        for (int i = 0; i < creativeTabs.length; i++)
                        {
                            if (creativeTabs[i] && index == i)
                            {
                                return true;
                            }
                        }
                    }
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
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
        tile.onFilterChanged();
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
                return StatCollector.translateToLocal(Strings.INGOT_FILTER);
            case 1:
                return StatCollector.translateToLocal(Strings.ORE_FILTER);
            case 2:
                return StatCollector.translateToLocal(Strings.LOG_FILTER);
            case 3:
                return StatCollector.translateToLocal(Strings.PLANK_FILTER);
            case 4:
                return StatCollector.translateToLocal(Strings.DUST_FILTER);
            case 5:
                return StatCollector.translateToLocal(Strings.CRUSHED_ORE_FILTER);
            case 6:
                return StatCollector.translateToLocal(Strings.PURIFIED_ORE_FILTER);
            case 7:
                return StatCollector.translateToLocal(Strings.PLATE_FILTER);
            case 8:
                return StatCollector.translateToLocal(Strings.GEM_FILTER);
            case 9:
                return StatCollector.translateToLocal(Strings.FOOD_FILTER);
            case 10:
                return StatCollector.translateToLocal(Strings.DYE_FILTER);
            case 11:
                return StatCollector.translateToLocal(Strings.NUGGET_FILTER);
            case 12:
                return StatCollector.translateToLocal(Strings.PLANT_FILTER);
            case 13:
                return StatCollector.translateToLocal(Strings.FUEL_FILTER);
            default:
                return I18n.format(tabs[getCreativeTab(place)].getTranslatedTabLabel()).replace("itemGroup.", "");
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

    public boolean isBlacklisting()
    {
        return blacklists;
    }

    public void setBlacklists(boolean blacklists)
    {
        this.blacklists = blacklists;
        tile.onFilterChanged();
    }

    public String getUserFilter()
    {
        return userFilter;
    }

    public void setUserFilter(String userFilter)
    {
        this.userFilter = userFilter;
        tile.onFilterChanged();
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setString("userFilter", getUserFilter());
        compound.setBoolean("blacklists", isBlacklisting());
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
        this.userFilter = compound.getString("userFilter");
        this.blacklists = compound.getBoolean("blacklists");
        for (int i = 0; i < customFilters.length; i++)
        {
            customFilters[i] = compound.getBoolean("cumstomFilters" + i);
        }
        for (int i = 0; i < creativeTabs.length; i++)
        {
            creativeTabs[i] = compound.getBoolean("creativeTabs" + i);
        }
    }

    public static boolean stringMatchesWildcardPattern(String string, String wildcardPattern)
    {
        if (wildcardPattern.startsWith("*") && wildcardPattern.length() > 1)
        {
            if (wildcardPattern.endsWith("*") && wildcardPattern.length() > 2)
            {
                if (string.contains(wildcardPattern.substring(1, wildcardPattern.length() - 1)))
                    return true;
            }
            else if (string.endsWith(wildcardPattern.substring(1)))
                return true;
        }
        else if (wildcardPattern.endsWith("*") && wildcardPattern.length() > 1)
        {
            if (string.startsWith(wildcardPattern.substring(0, wildcardPattern.length() - 1)))
                return true;
        }
        else
        {
            if (string.equalsIgnoreCase(wildcardPattern))
                return true;
        }
        return false;
    }

    public static String[] getOreNames(ItemStack itemStack)
    {
        int[] ids = OreDictionary.getOreIDs(itemStack);

        String[] oreNames = new String[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            oreNames[i] = OreDictionary.getOreName(ids[i]).toLowerCase().replaceAll("\\s+", "");
        }
        return oreNames;
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

    public static String[] getLabels()
    {
        String[] labels = new String[CreativeTabs.creativeTabArray.length];
        CreativeTabs[] creativeTabArray = CreativeTabs.creativeTabArray;
        for (int i = 0; i < creativeTabArray.length; i++)
        {
            try
            {
                labels[i] = (String) tabLabel.get(creativeTabArray[i]);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        return labels;
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
