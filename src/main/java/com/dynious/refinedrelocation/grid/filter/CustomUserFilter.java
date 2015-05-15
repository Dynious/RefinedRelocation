package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.grid.MultiFilter;
import com.dynious.refinedrelocation.helper.LogHelper;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterString;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CustomUserFilter extends AbstractFilter {

    private String value = "";

    public CustomUserFilter(MultiFilter parent, int index) {
        super(TYPE_CUSTOM, parent, index);
    }

    @Override
    public boolean isInFilter(ItemStack itemStack) {
        String[] oreNames = null;
        String filter = value.toLowerCase().replaceAll("\\s+", "");
        String itemName = null;
        for(String s : filter.split(",")) {
            if (s.contains("!")) {
                if(oreNames == null) {
                    oreNames = MultiFilter.getOreNames(itemStack);
                }
                s = s.replace("!", "");
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
    public void setFilterString(int optionId, String value) {
        switch(optionId) {
            case 0: this.value = value; break;
        }
    }
}
