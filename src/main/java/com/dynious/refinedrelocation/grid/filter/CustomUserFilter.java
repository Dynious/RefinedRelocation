package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.client.graphics.TextureRegion;
import com.dynious.refinedrelocation.client.gui.SharedAtlas;
import com.dynious.refinedrelocation.client.gui.widget.GuiUserFilter;
import com.dynious.refinedrelocation.grid.MultiFilter;
import com.dynious.refinedrelocation.helper.LogHelper;
import com.dynious.refinedrelocation.lib.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomUserFilter extends MultiFilterChildBase {
    public static final String TYPE_NAME = "user";

    private static TextureRegion iconTexture;

    private String value = "";

    @Override
    public boolean isInFilter(ItemStack itemStack) {
        // TODO cache patterns instead of re-compiling them all the time
        String[] oreNames = null;
        String filter = value.toLowerCase();
        String itemName = null;
        for (String s : filter.split("[\n,]")) {
            boolean isOreDict = s.startsWith("!");
            if(isOreDict) {
                s = s.substring(1);
            }
            Pattern pattern = Pattern.compile(getRegexWildcardPattern(s));
            Matcher matcher = pattern.matcher("");
            if (isOreDict) {
                if (oreNames == null) {
                    oreNames = MultiFilter.getOreNames(itemStack);
                }
                for (String oreName : oreNames) {
                    if(matcher.reset(oreName).find()) {
                        return true;
                    }
                }
            } else {
                if (itemName == null) {
                    try {
                        itemName = itemStack.getDisplayName().toLowerCase();
                    } catch (Exception e) {
                        LogHelper.error("Encountered an error when retrieving item name of: " + itemStack.toString());
                        break;
                    }
                }
                if(matcher.reset(itemName).find()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final Pattern WILDCARD_PATTERN = Pattern.compile("[^*]+|(\\*)");
    private static String getRegexWildcardPattern(String s) {
        Matcher matcher = WILDCARD_PATTERN.matcher(s);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            if(matcher.group(1) != null) {
                matcher.appendReplacement(sb, ".*");
            } else {
                matcher.appendReplacement(sb, "\\\\Q" + matcher.group(0) + "\\\\E");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("value", value);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        value = compound.getString("value");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        markDirty(true);
    }

    @Override
    public void sendUpdate(EntityPlayerMP playerMP) {
        getParentFilter().sendStringToPlayer(this, playerMP, 0, value);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IGuiWidgetWrapped getGuiWidget(int x, int y, int width, int height) {
        return new GuiUserFilter(x, y, width, height, true, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getIconSheet() {
        if (iconTexture == null) {
            iconTexture = SharedAtlas.findRegion("icon_filter_custom");
        }
        return iconTexture.texture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconX() {
        return iconTexture.getRegionX();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconY() {
        return iconTexture.getRegionY();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconWidth() {
        return iconTexture.getRegionWidth();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconHeight() {
        return iconTexture.getRegionHeight();
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public void setFilterString(int optionId, String value) {
        switch (optionId) {
            case 0:
                this.value = value;
                break;
        }
    }

    @Override
    public void setFilterBoolean(int optionId, boolean value) {
    }

    @Override
    public void setFilterBooleanArray(int optionId, boolean[] values) {
    }

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
