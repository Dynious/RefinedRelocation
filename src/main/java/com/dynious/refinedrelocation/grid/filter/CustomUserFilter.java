package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.client.gui.widget.GuiUserFilter;
import com.dynious.refinedrelocation.grid.MultiFilter;
import com.dynious.refinedrelocation.helper.LogHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterString;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CustomUserFilter implements IMultiFilterChild {

    public static final String TYPE_NAME = "user";
    private IMultiFilter parentFilter;
    private int filterIndex;
    private boolean isDirty;
    private String value = "";

    @Override
    public boolean isInFilter(ItemStack itemStack) {
        String[] oreNames = null;
        String filter = value.toLowerCase().replaceAll("\\s+", "");
        String itemName = null;
        for(String s : filter.split("[\n,]")) {
            if (s.startsWith("!")) {
                if(oreNames == null) {
                    oreNames = MultiFilter.getOreNames(itemStack);
                }
                s = s.substring(1);
                for(String oreName : oreNames) {
                    if(stringMatchesWildcardPattern(oreName, s)) {
                        return true;
                    }
                }
            } else {
                if(itemName == null) {
                    try {
                        itemName = itemStack.getDisplayName().toLowerCase().replaceAll("\\s+", "");
                    } catch (Exception e) {
                        LogHelper.error("Encountered an error when retrieving item name of: " + itemStack.toString());
                        break;
                    }
                }
                if(stringMatchesWildcardPattern(itemName, s)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setString("value", value);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        value = compound.getString("value");
    }

    @Override
    public void markDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        markDirty(true);
    }

    // TODO move this function to a better place
    private static boolean stringMatchesWildcardPattern(String string, String wildcardPattern) {
        // TODO this function only allows wildcards at the beginning and/or end, not in the middle
        if(wildcardPattern.startsWith("*") && wildcardPattern.length() > 1) {
            if(wildcardPattern.endsWith("*") && wildcardPattern.length() > 2) {
                if(string.contains(wildcardPattern.substring(1, wildcardPattern.length() - 1))) {
                    return true;
                }
            } else if(string.endsWith(wildcardPattern.substring(1))) {
                return true;
            }
        } else if(wildcardPattern.endsWith("*") && wildcardPattern.length() > 1) {
            if (string.startsWith(wildcardPattern.substring(0, wildcardPattern.length() - 1))) {
                return true;
            }
        } else {
            if(string.equalsIgnoreCase(wildcardPattern)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void sendUpdate(EntityPlayerMP playerMP) {
        NetworkHandler.INSTANCE.sendTo(new MessageSetFilterString(filterIndex, 0, value), playerMP);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IGuiWidgetWrapped getGuiWidget(int x, int y, int width, int height) {
        return new GuiUserFilter(x, y, width, height, true, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getIconSheet() {
        return Resources.GUI_SHARED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconX() {
        return 116;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconY() {
        return 238;
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public void setParentFilter(IMultiFilter parentFilter, int filterIndex) {
        this.parentFilter = parentFilter;
        this.filterIndex = filterIndex;
    }

    @Override
    public IMultiFilter getParentFilter() {
        return parentFilter;
    }

    @Override
    public int getFilterIndex() {
        return filterIndex;
    }

    @Override
    public void setFilterString(int optionId, String value) {
        switch(optionId) {
            case 0: this.value = value; break;
        }
    }

    @Override
    public void setFilterBoolean(int optionId, boolean value) {}

    @Override
    public void setFilterBooleanArray(int optionId, boolean[] values) {}

    @Override
    @SideOnly(Side.CLIENT)
    public String getNameLangKey() {
        return Strings.CUSTOM_FILTER;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getDescriptionLangKey() {
        return Strings.CUSTOM_FILTER_DESCRIPTION;
    }
}
