package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.grid.MultiFilter;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterBooleanArray;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class CreativeTabFilter extends AbstractFilter implements IChecklistFilter {

    public static class ServerSideCreativeTab {
        public final int tabIndex;
        public final String tabLabel;

        public ServerSideCreativeTab(int tabIndex, String tabLabel) {
            this.tabIndex = tabIndex;
            this.tabLabel = tabLabel;
        }
    }

    public static ServerSideCreativeTab[] serverSideTabs;

    public static void syncTabs(String[] tabLabels) {
        serverSideTabs = new ServerSideCreativeTab[tabLabels.length];
        for (int i = 0; i < tabLabels.length; i++) {
            serverSideTabs[i] = new ServerSideCreativeTab(i, tabLabels[i]);
        }
    }

    private boolean[] tabStates;

    public CreativeTabFilter(MultiFilter parent, int index) {
        super(TYPE_CREATIVETAB, parent, index);
        if(serverSideTabs == null) {
            serverSideTabs = new ServerSideCreativeTab[CreativeTabs.creativeTabArray.length];
            for(int i = 0; i < serverSideTabs.length; i++) {
                serverSideTabs[i] = new ServerSideCreativeTab(i, CreativeTabs.creativeTabArray[i].tabLabel);
            }
        }
        tabStates = new boolean[serverSideTabs.length];
    }

    @Override
    public boolean isInFilter(ItemStack itemStack) {
        CreativeTabs tab;
        if(itemStack.getItem() instanceof ItemBlock) {
            tab = Block.getBlockById(ItemBlock.getIdFromItem(itemStack.getItem())).displayOnCreativeTab;
        } else {
            tab = itemStack.getItem().tabToDisplayOn;
        }
        if (tab != null)
        {
            int index = tab.tabIndex;

            for (int i = 0; i < tabStates.length; i++)
            {
                if (tabStates[i] && index == i)
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        NBTTagList tagList = new NBTTagList();
        for(int i = 0; i < tabStates.length; i++) {
            if(tabStates[i]) {
                tagList.appendTag(new NBTTagString(serverSideTabs[i].tabLabel));
            }
        }
        compound.setTag("tabStates", tagList);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        NBTTagList tagList = compound.getTagList("tabStates", 8);
        for(int i = 0; i < tagList.tagCount(); i++) {
            String tabLabel = tagList.getStringTagAt(i);
            for(int j = 0; j < serverSideTabs.length; j++) {
                if(serverSideTabs[j].tabLabel.equals(tabLabel)) {
                    tabStates[j] = true;
                    break;
                }
            }
        }
    }

    @Override
    public void sendUpdate(EntityPlayerMP playerMP) {
        NetworkHandler.INSTANCE.sendTo(new MessageSetFilterBooleanArray(filterIndex, 0, tabStates), playerMP);
    }

    @Override
    public void setFilterBooleanArray(int optionId, boolean[] values) {
        tabStates = values;
    }

    @Override
    public void setFilterBoolean(int optionId, boolean value) {
        tabStates[optionId] = value;
    }

    @Override
    public String getFilterName() {
        return Strings.CREATIVE_FILTER;
    }

    @Override
    public String getName(int index) {
        return I18n.format("itemGroup." + serverSideTabs[index].tabLabel).replace("itemGroup.", "");
    }

    @Override
    public void setValue(int optionIndex, boolean value) {
        tabStates[optionIndex] = value;
        markDirty(true);
    }

    @Override
    public boolean getValue(int optionIndex) {
        return tabStates[optionIndex];
    }

    @Override
    public int getOptionCount() {
        return tabStates.length;
    }

    public static String[] getCreativeTabLabels() {
        String[] labels = new String[CreativeTabs.creativeTabArray.length];
        CreativeTabs[] creativeTabArray = CreativeTabs.creativeTabArray;
        for (int i = 0; i < creativeTabArray.length; i++)
        {
            labels[i] = creativeTabArray[i].tabLabel;
        }
        return labels;
    }
}
