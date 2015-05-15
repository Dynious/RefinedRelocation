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

    public MultiFilter(IFilterTileGUI tile) {
        this.tile = tile;
    }

    // TODO find a better place for this function
    public static String[] getOreNames(ItemStack itemStack) {
        int[] ids = OreDictionary.getOreIDs(itemStack);
        String[] oreNames = new String[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            oreNames[i] = OreDictionary.getOreName(ids[i]).toLowerCase().replaceAll("\\s+", "");
        }
        return oreNames;
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
    public boolean isBlacklisting() {
        return isBlacklisting;
    }

    @Override
    public void setBlacklists(boolean blacklists) {
        this.isBlacklisting = blacklists;
        tile.onFilterChanged();
    }

    @Override
    public void filterChanged() {
        tile.onFilterChanged();
    }

    @Override
    public List<String> getWAILAInformation(NBTTagCompound nbtData) {
        List<String> filter = new ArrayList<String>();

        filter.add(StringHelper.getLocalizedString(Strings.MODE) + ": " + (nbtData.getBoolean("blacklists") ? StringHelper.getLocalizedString(Strings.BLACKLIST) : StringHelper.getLocalizedString(Strings.WHITELIST)));

        // TODO add back filter-specific WAILA information

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
                        CustomUserFilter userFilter = new CustomUserFilter(this, filterList.size());
                        userFilter.readFromNBT(tagFilter);
                        filterList.add(userFilter);
                        break;
                    // Preset Filter
                    case 1:
                        PresetFilter presetFilter = new PresetFilter(this, filterList.size());
                        presetFilter.readFromNBT(tagFilter);
                        filterList.add(presetFilter);
                        break;
                    // Creative Tab Filter
                    case 2:
                        CreativeTabFilter creativeTabFilter = new CreativeTabFilter(this, filterList.size());
                        creativeTabFilter.readFromNBT(tagFilter);
                        filterList.add(creativeTabFilter);
                        break;
                }
            }
        } else {
            this.isBlacklisting = compound.getBoolean("blacklists");

            String userFilterString = compound.getString("userFilter");
            if(!userFilterString.isEmpty()) {
                CustomUserFilter userFilter = new CustomUserFilter(this, filterList.size());
                userFilter.setValue(userFilterString);
                filterList.add(userFilter);
            }

            PresetFilter presetFilter = new PresetFilter(this, filterList.size());
            boolean foundActive = false;
            for (int i = 0; i < PresetFilter.PRESET_COUNT; i++) {
                if(compound.getBoolean("cumstomFilters" + i)) {
                    foundActive = true;
                    presetFilter.setValue(i, true);
                }
            }
            if(foundActive) {
                filterList.add(presetFilter);
            }

            String creativeTabFilters = compound.getString("filters");
            CreativeTabFilter creativeTabFilter = new CreativeTabFilter(this, filterList.size());
            foundActive = false;
            for(String string : creativeTabFilters.split("\\^\\$")) {
                for(int i = 0; i < CreativeTabFilter.serverSideTabs.length; i++) {
                    if(string.equals(CreativeTabFilter.serverSideTabs[i].tabLabel)) {
                        creativeTabFilter.setValue(i, true);
                        foundActive = true;
                    }
                }
            }
            if(foundActive) {
                filterList.add(creativeTabFilter);
            } else {
                //1.0.7- compat
                for (int i = 0; compound.hasKey("creativeTabs" + i); i++) {
                    if(compound.getBoolean("creativeTabs" + i)) {
                        creativeTabFilter.setValue(i, true);
                        foundActive = true;
                    }
                }
                if(foundActive) {
                    filterList.add(creativeTabFilter);
                }
            }
        }
    }

    @Override
    public int getFilterCount() {
        return filterList.size();
    }

    @Override
    public AbstractFilter getFilterAtIndex(int index) {
        return filterList.get(index);
    }

}
