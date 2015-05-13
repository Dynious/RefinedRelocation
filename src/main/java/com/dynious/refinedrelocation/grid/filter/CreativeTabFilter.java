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

    private boolean[] creativeTabs;

    public CreativeTabFilter() {
        super(TYPE_CREATIVETAB);
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

            for (int i = 0; i < creativeTabs.length; i++)
            {
                if (creativeTabs[i] && index == i)
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
        for(int i = 0; i < creativeTabs.length; i++) {
            if(creativeTabs[i]) {
                tagList.appendTag(new NBTTagString(serverSideTabs[i].tabLabel));
            }
        }
        compound.setTag("creativeTabs", tagList);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

    }

    public String getName(int index) {
        return I18n.format("itemGroup" + serverSideTabs[index].tabLabel).replace("itemGroup.", "");
    }

    public void setTabActive(int index, boolean active) {
        creativeTabs[index] = active;
    }

    public boolean isTabActive(int index) {
        return creativeTabs[index];
    }

}
