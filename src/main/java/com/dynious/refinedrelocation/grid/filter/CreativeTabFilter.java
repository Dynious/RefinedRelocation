package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.api.filter.IChecklistFilter;
import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.client.gui.widget.GuiFilterList;
import com.dynious.refinedrelocation.event.InitialSyncHandler;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

public class CreativeTabFilter extends MultiFilterChildBase implements IChecklistFilter
{
    public static final String TYPE_NAME = "creative";

    public static String[] serverSideTabLabels;

    public static void syncTabLabels(String[] tabLabels)
    {
        serverSideTabLabels = tabLabels;
    }

    private boolean[] tabStates;

    public CreativeTabFilter()
    {
        if (serverSideTabLabels == null)
        {
            serverSideTabLabels = InitialSyncHandler.getCreativeTabLabels();
        }
        tabStates = new boolean[serverSideTabLabels.length];
    }

    @Override
    public boolean isInFilter(ItemStack itemStack)
    {
        CreativeTabs tab;
        if (itemStack.getItem() instanceof ItemBlock)
        {
            tab = Block.getBlockById(ItemBlock.getIdFromItem(itemStack.getItem())).displayOnCreativeTab;
        } else
        {
            tab = itemStack.getItem().tabToDisplayOn;
        }
        if (tab != null)
        {
            return tabStates[tab.tabIndex];
        }
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < tabStates.length; i++)
        {
            if (tabStates[i])
            {
                tagList.appendTag(new NBTTagString(serverSideTabLabels[i]));
            }
        }
        compound.setTag("tabStates", tagList);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList tagList = compound.getTagList("tabStates", 8);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            String tabLabel = tagList.getStringTagAt(i);
            for (int j = 0; j < serverSideTabLabels.length; j++)
            {
                if (serverSideTabLabels[j].equals(tabLabel))
                {
                    tabStates[j] = true;
                    break;
                }
            }
        }
    }

    @Override
    public void sendUpdate(EntityPlayerMP playerMP)
    {
        getParentFilter().sendBooleanArrayToPlayer(this, playerMP, 0, tabStates);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IGuiWidgetWrapped getGuiWidget(int x, int y, int width, int height)
    {
        return new GuiFilterList(x, y, width, height, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getIconSheet()
    {
        return Resources.GUI_SHARED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconX()
    {
        return 80;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconY()
    {
        return 238;
    }

    @Override
    public void setFilterBooleanArray(int optionId, boolean[] values)
    {
        tabStates = values;
    }

    @Override
    public void setFilterBoolean(int optionId, boolean value)
    {
        tabStates[optionId] = value;
    }

    @Override
    public void setFilterString(int optionId, String value)
    {
    }

    @Override
    public String getName(int index)
    {
        return I18n.format("itemGroup." + serverSideTabLabels[index]).replace("itemGroup.", "");
    }

    @Override
    public void setValue(int optionIndex, boolean value)
    {
        tabStates[optionIndex] = value;
        markDirty(true);
    }

    @Override
    public boolean getValue(int optionIndex)
    {
        return tabStates[optionIndex];
    }

    @Override
    public int getOptionCount()
    {
        return tabStates.length;
    }

    @Override
    public String getTypeName()
    {
        return TYPE_NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getNameLangKey()
    {
        return Strings.CREATIVE_FILTER;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getDescriptionLangKey()
    {
        return Strings.CREATIVE_FILTER_DESCRIPTION;
    }
}
