package com.dynious.refinedrelocation.grid;

import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.grid.filter.CreativeTabFilter;
import com.dynious.refinedrelocation.grid.filter.CustomUserFilter;
import com.dynious.refinedrelocation.grid.filter.MultiFilterRegistry;
import com.dynious.refinedrelocation.grid.filter.PresetFilter;
import com.dynious.refinedrelocation.helper.StringHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterBoolean;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterBooleanArray;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterString;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class MultiFilter implements IMultiFilter
{
    private IFilterTileGUI tile;
    private boolean isBlacklisting;

    private boolean isDirty;

    private List<IMultiFilterChild> filterList = new ArrayList<IMultiFilterChild>();

    public MultiFilter(IFilterTileGUI tile)
    {
        this.tile = tile;
    }

    // TODO find a better place for this function
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

    @Override
    public boolean passesFilter(ItemStack itemStack)
    {
        if (itemStack == null)
        {
            return false;
        }
        boolean foundInFilter = false;
        for (IMultiFilterChild filter : filterList)
        {
            if (filter.isInFilter(itemStack))
            {
                foundInFilter = true;
                break;
            }
        }
        return isBlacklisting ? !foundInFilter : foundInFilter;
    }

    @Override
    public boolean isBlacklisting()
    {
        return isBlacklisting;
    }

    @Override
    public void setBlacklists(boolean blacklists)
    {
        this.isBlacklisting = blacklists;
        tile.onFilterChanged();
    }

    @Override
    public void filterChanged()
    {
        tile.onFilterChanged();
    }

    @Override
    public List<String> getWAILAInformation(NBTTagCompound nbtData)
    {
        List<String> filter = new ArrayList<String>();

        filter.add(StringHelper.getLocalizedString(Strings.MODE) + ": " + (nbtData.getBoolean("isBlacklisting") ? StringHelper.getLocalizedString(Strings.BLACKLIST) : StringHelper.getLocalizedString(Strings.WHITELIST)));

        // TODO add back filter-specific WAILA information

        return filter;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setByte("version", (byte) 1);
        compound.setBoolean("isBlacklisting", isBlacklisting);
        NBTTagList tagFilterList = new NBTTagList();
        for (IMultiFilterChild filter : filterList)
        {
            NBTTagCompound tagFilter = new NBTTagCompound();
            tagFilter.setString("type", filter.getTypeName());
            filter.writeToNBT(tagFilter);
            tagFilterList.appendTag(tagFilter);
        }
        compound.setTag("filterList", tagFilterList);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        filterList.clear();
        if (compound.getByte("version") >= 1)
        {
            isBlacklisting = compound.getBoolean("isBlacklisting");
            NBTTagList tagFilterList = compound.getTagList("filterList", 10);
            for (int i = 0; i < tagFilterList.tagCount(); i++)
            {
                NBTTagCompound tagFilter = tagFilterList.getCompoundTagAt(i);
                String filterType = tagFilter.getString("type");
                IMultiFilterChild filter = MultiFilterRegistry.getFilter(filterType);
                if (filter != null)
                {
                    filter.setParentFilter(this, filterList.size());
                    filter.readFromNBT(tagFilter);
                    filterList.add(filter);
                }
            }
        } else
        {
            this.isBlacklisting = compound.getBoolean("blacklists");

            String userFilterString = compound.getString("userFilter");
            if (!userFilterString.isEmpty())
            {
                CustomUserFilter userFilter = new CustomUserFilter();
                userFilter.setParentFilter(this, filterList.size());
                userFilter.setValue(userFilterString);
                filterList.add(userFilter);
            }

            PresetFilter presetFilter = new PresetFilter();
            presetFilter.setParentFilter(this, filterList.size());
            boolean foundActive = false;
            for (int i = 0; i < PresetFilter.PRESET_COUNT; i++)
            {
                if (compound.getBoolean("cumstomFilters" + i))
                {
                    foundActive = true;
                    presetFilter.setValue(i, true);
                }
            }
            if (foundActive)
            {
                filterList.add(presetFilter);
            }

            String creativeTabFilters = compound.getString("filters");
            CreativeTabFilter creativeTabFilter = new CreativeTabFilter();
            creativeTabFilter.setParentFilter(this, filterList.size());
            foundActive = false;
            for (String string : creativeTabFilters.split("\\^\\$"))
            {
                for (int i = 0; i < CreativeTabFilter.serverSideTabs.length; i++)
                {
                    if (string.equals(CreativeTabFilter.serverSideTabs[i].tabLabel))
                    {
                        creativeTabFilter.setValue(i, true);
                        foundActive = true;
                    }
                }
            }
            if (foundActive)
            {
                filterList.add(creativeTabFilter);
            } else
            {
                //1.0.7- compat
                for (int i = 0; compound.hasKey("creativeTabs" + i); i++)
                {
                    if (compound.getBoolean("creativeTabs" + i))
                    {
                        creativeTabFilter.setValue(i, true);
                        foundActive = true;
                    }
                }
                if (foundActive)
                {
                    filterList.add(creativeTabFilter);
                }
            }
        }
    }

    @Override
    public int getFilterCount()
    {
        return filterList.size();
    }

    @Override
    public IMultiFilterChild getFilterAtIndex(int index)
    {
        return filterList.get(index);
    }

    @Override
    public IFilterTileGUI getFilterTile()
    {
        return tile;
    }

    @Override
    public void setFilterType(int filterIndex, String filterType)
    {
        if (filterIndex == -1)
        {
            filterIndex = filterList.size();
        } else if (filterIndex < filterList.size() && filterList.get(filterIndex).getTypeName().equals(filterType))
        {
            return;
        }
        if (filterType.isEmpty())
        {
            filterList.remove(filterIndex);
            for (int i = filterIndex; i < filterList.size(); i++)
            {
                filterList.get(i).setParentFilter(this, i);
            }
            markDirty(true);
        } else
        {
            IMultiFilterChild filter = MultiFilterRegistry.getFilter(filterType);
            if (filter != null)
            {
                filter.setParentFilter(this, filterIndex);
                markDirty(true);
                if (filterIndex < filterList.size())
                {
                    filterList.set(filterIndex, filter);
                } else
                {
                    filterList.add(filter);
                }
            }
        }
    }

    @Override
    public boolean isDirty()
    {
        return isDirty;
    }

    @Override
    public void markDirty(boolean dirty)
    {
        this.isDirty = dirty;
        filterChanged();
    }

    @Override
    public void sendStringToPlayer(IMultiFilterChild receiver, EntityPlayerMP player, int index, String value)
    {
        NetworkHandler.INSTANCE.sendTo(new MessageSetFilterString(receiver.getFilterIndex(), index, value), player);
    }

    @Override
    public void sendStringToServer(IMultiFilterChild receiver, int index, String value)
    {
        NetworkHandler.INSTANCE.sendToServer(new MessageSetFilterString(receiver.getFilterIndex(), index, value));
    }

    @Override
    public void sendBooleanToPlayer(IMultiFilterChild receiver, EntityPlayerMP player, int index, boolean value)
    {
        NetworkHandler.INSTANCE.sendTo(new MessageSetFilterBoolean(receiver.getFilterIndex(), index, value), player);
    }

    @Override
    public void sendBooleanToServer(IMultiFilterChild receiver, int index, boolean value)
    {
        NetworkHandler.INSTANCE.sendToServer(new MessageSetFilterBoolean(receiver.getFilterIndex(), index, value));
    }

    @Override
    public void sendBooleanArrayToPlayer(IMultiFilterChild receiver, EntityPlayerMP player, int index, boolean[] value)
    {
        NetworkHandler.INSTANCE.sendTo(new MessageSetFilterBooleanArray(receiver.getFilterIndex(), index, value), player);
    }

    @Override
    public void sendBooleanArrayToServer(IMultiFilterChild receiver, int index, boolean[] value)
    {
        NetworkHandler.INSTANCE.sendToServer(new MessageSetFilterBooleanArray(receiver.getFilterIndex(), index, value));
    }
}
