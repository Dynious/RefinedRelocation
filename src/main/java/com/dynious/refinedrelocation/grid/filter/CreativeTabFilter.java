package com.dynious.refinedrelocation.grid.filter;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class CreativeTabFilter extends AbstractFilter {

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

    public CreativeTabFilter() {
        super(TYPE_CREATIVETAB);
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

    public String getName(int index) {
        return I18n.format("itemGroup." + serverSideTabs[index].tabLabel).replace("itemGroup.", "");
    }

    public void setTabActive(int index, boolean active) {
        tabStates[index] = active;
    }

    public boolean isTabActive(int index) {
        return tabStates[index];
    }

}
