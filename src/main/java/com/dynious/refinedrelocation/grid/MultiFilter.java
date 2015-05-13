package com.dynious.refinedrelocation.grid;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.grid.filter.AbstractFilter;
import com.dynious.refinedrelocation.grid.filter.CreativeTabFilter;
import com.dynious.refinedrelocation.grid.filter.CustomUserFilter;
import com.dynious.refinedrelocation.grid.filter.PresetFilter;
import com.dynious.refinedrelocation.helper.StringHelper;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MultiFilter implements IFilterGUI {

    private IFilterTileGUI tile;
    private boolean isBlacklisting = false;

    private List<AbstractFilter> filterList = new ArrayList<AbstractFilter>();

    // Until the filters are dynamic as described in #150, store them the old way too
    private CustomUserFilter userFilter;
    private PresetFilter presetFilter;
    private CreativeTabFilter creativeTabFilter;

    public MultiFilter(IFilterTileGUI tile)
    {
        this.tile = tile;
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

    public static String[] getLabels()
    {
        String[] labels = new String[CreativeTabs.creativeTabArray.length];
        CreativeTabs[] creativeTabArray = CreativeTabs.creativeTabArray;
        for (int i = 0; i < creativeTabArray.length; i++)
        {
            labels[i] = creativeTabArray[i].tabLabel;
        }
        return labels;
    }

    @Override
    @Deprecated
    public int getSize() {
        return CreativeTabFilter.serverSideTabs.length - 2 + PresetFilter.PRESET_COUNT;
    }

    @Override
    public boolean passesFilter(ItemStack itemStack) {
        if(itemStack == null) {
            return false;
        }
        boolean foundInFilter = false;
        for(AbstractFilter filter : filterList) {
            if(filter.isInFilter(itemStack)) {
                foundInFilter = true;
                break;
            }
        }
        return isBlacklisting ? !foundInFilter : foundInFilter;
    }

    @Override
    @Deprecated
    public void setValue(int listIndex, boolean value) {
        if(listIndex < PresetFilter.PRESET_COUNT) {
            presetFilter.setPresetActive(listIndex, value);
        } else {
            creativeTabFilter.setTabActive(listIndex - PresetFilter.PRESET_COUNT, value);
        }
        tile.onFilterChanged();
    }

    @Override
    @Deprecated
    public boolean getValue(int listIndex) {
        if(listIndex < PresetFilter.PRESET_COUNT) {
            return presetFilter.isPresetActive(listIndex);
        } else {
            return creativeTabFilter.isTabActive(listIndex - PresetFilter.PRESET_COUNT);
        }
    }

    @Override
    @Deprecated
    public String getName(int listIndex) {
        if(listIndex < PresetFilter.PRESET_COUNT) {
            presetFilter.getName(listIndex);
        } else {
            creativeTabFilter.getName(listIndex - PresetFilter.PRESET_COUNT);
        }
        return null;
    }

    @Override
    public boolean isBlacklisting() {
        return isBlacklisting;
    }

    @Override
    public void setBlacklists(boolean blacklists) {
        this.isBlacklisting = blacklists;
        tile.onFilterChanged();
    }

    @Override
    public String getUserFilter() {
        return userFilter.getValue();
    }

    @Override
    public void setUserFilter(String value) {
        userFilter.setValue(value);
        tile.onFilterChanged();
    }

    @Override
    public List<String> getWAILAInformation(NBTTagCompound nbtData) {
        List<String> filter = new ArrayList<String>();

        filter.add(StringHelper.getLocalizedString(Strings.MODE) + ": " + (nbtData.getBoolean("blacklists") ? StringHelper.getLocalizedString(Strings.BLACKLIST) : StringHelper.getLocalizedString(Strings.WHITELIST)));

        if (nbtData.hasKey("userFilter") && StringUtils.isNotBlank(nbtData.getString("userFilter")))
        {
            filter.add(StatCollector.translateToLocalFormatted(Strings.WAILA_USER_FILTER, StringUtils.abbreviate(nbtData.getString("userFilter"), 40)));
        }

        int usedPresets = 0;
        for (int i = 0; i < getSize(); i++)
        {
            if (usedPresets < 2) // Only show a maximum of 2 presets
            {
                if (getValue(i))
                {
                    filter.add(" " + getName(i) + (i == 1 ? " " + StringHelper.getLocalizedString(Strings.ELLIPSE) : ""));
                    ++usedPresets;
                }
            }
            else
            {
                break;
            }
        }
        return filter;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setByte("version", (byte) 1);
        compound.setBoolean("isBlacklisting", isBlacklisting);
        NBTTagList tagFilterList = new NBTTagList();
        for(AbstractFilter filter : filterList) {
            NBTTagCompound tagFilter = new NBTTagCompound();
            tagFilter.setByte("type", filter.getTypeId());
            filter.writeToNBT(tagFilter);
            tagFilterList.appendTag(tagFilter);
        }
        compound.setTag("filterList", tagFilterList);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if(compound.getByte("version") >= 1) {
            isBlacklisting = compound.getBoolean("isBlacklisting");
            NBTTagList tagFilterList = compound.getTagList("filterList", 10);
            for(int i = 0; i < tagFilterList.tagCount(); i++) {
                NBTTagCompound tagFilter = tagFilterList.getCompoundTagAt(i);
                int filterType = tagFilter.getByte("type");
                switch(filterType) {
                    // Custom User Filter
                    case AbstractFilter.TYPE_CUSTOM:
                        CustomUserFilter userFilter = new CustomUserFilter();
                        userFilter.readFromNBT(tagFilter);
                        filterList.add(userFilter);

                        // Until the filters are dynamic as described in #150, store them the old way too
                        this.userFilter = userFilter;
                        break;
                    // Preset Filter
                    case 1:
                        PresetFilter presetFilter = new PresetFilter();
                        presetFilter.readFromNBT(tagFilter);
                        filterList.add(presetFilter);

                        this.presetFilter = presetFilter;
                        break;
                    // Creative Tab Filter
                    case 2:
                        CreativeTabFilter creativeTabFilter = new CreativeTabFilter();
                        creativeTabFilter.readFromNBT(tagFilter);
                        filterList.add(creativeTabFilter);

                        // Until the filters are dynamic as described in #150, store them the old way too
                        this.creativeTabFilter = creativeTabFilter;
                        break;
                }
            }
        } else {
            this.userFilter.setValue(compound.getString("userFilter"));
            this.isBlacklisting = compound.getBoolean("blacklists");
            for (int i = 0; i < PresetFilter.PRESET_COUNT; i++) {
                presetFilter.setPresetActive(i, compound.getBoolean("cumstomFilters" + i));
            }

            String filters = compound.getString("filters");
            for(String string : filters.split("\\^\\$")) {
                for(int i = 0; i < CreativeTabFilter.serverSideTabs.length; i++) {
                    if(string.equals(CreativeTabFilter.serverSideTabs[i].tabLabel))
                        creativeTabFilter.setTabActive(i, true);
                }
            }

            //1.0.7- compat
            for(int i = 0; compound.hasKey("creativeTabs" + i); i++) {
                creativeTabFilter.setTabActive(i, compound.getBoolean("creativeTabs" + i));
            }
        }
    }
}
