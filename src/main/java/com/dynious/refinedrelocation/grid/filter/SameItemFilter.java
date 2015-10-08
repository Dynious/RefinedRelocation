package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.client.graphics.TextureRegion;
import com.dynious.refinedrelocation.client.gui.SharedAtlas;
import com.dynious.refinedrelocation.client.gui.widget.GuiSameItemFilter;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class SameItemFilter extends MultiFilterChildBase
{
    public static final String TYPE_NAME = "sameItem";

    private static TextureRegion iconTexture;

    public boolean checkMetadata = true;
    public boolean checkNBTData = true;

    @Override
    public String getTypeName()
    {
        return TYPE_NAME;
    }

    @Override
    public boolean canFilterBeUsedOnTile(TileEntity tile)
    {
        return tile instanceof ISortingInventory;
    }

    @Override
    public boolean isInFilter(ItemStack itemStack)
    {
        if (getParentFilter().getFilterTile().getTileEntity() instanceof ISortingInventory)
        {
            ISortingInventory sortingInventory = (ISortingInventory) getParentFilter().getFilterTile().getTileEntity();
            for (int i = 0; i < sortingInventory.getSizeInventory(); i++)
            {
                if (ItemStackHelper.areItemStacksEqual(sortingInventory.getStackInSlot(i), itemStack, checkMetadata, checkNBTData))
                    return true;
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IGuiWidgetWrapped getGuiWidget(int x, int y, int width, int height)
    {
        return new GuiSameItemFilter(this, x, y, width, height);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getIconSheet()
    {
        if(iconTexture == null) {
            iconTexture = SharedAtlas.findRegion("icon_filter_sameitem");
        }
        return iconTexture.texture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconX()
    {
        return iconTexture.getRegionX();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconY()
    {
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
    @SideOnly(Side.CLIENT)
    public String getNameLangKey()
    {
        return Strings.SAME_ITEM_FILTER;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getDescriptionLangKey()
    {
        return Strings.SAME_ITEM_FILTER_DESCRIPTION;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("checkMetadata", checkMetadata);
        compound.setBoolean("checkNBTData", checkNBTData);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        checkMetadata = compound.getBoolean("checkMetadata");
        checkNBTData = compound.getBoolean("checkNBTData");
    }

    @Override
    public void setFilterBoolean(int optionId, boolean value)
    {
        switch (optionId)
        {
            case 0:
                checkMetadata = value;
                break;
            case 1:
                checkNBTData = value;
                break;
        }
    }
}
