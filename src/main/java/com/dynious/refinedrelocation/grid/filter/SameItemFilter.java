package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.api.filter.FilterResult;
import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.client.gui.widget.GuiSameItemFilter;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class SameItemFilter extends MultiFilterChildBase
{
    public static final String TYPE_NAME = "sameItem";

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
    public void passesFilter(ItemStack itemStack, FilterResult outResult)
    {
        if (getParentFilter().getFilterTile().getTileEntity() instanceof ISortingInventory)
        {
            ISortingInventory sortingInventory = (ISortingInventory) getParentFilter().getFilterTile().getTileEntity();
            for (int i = 0; i < sortingInventory.getSizeInventory(); i++)
            {
                if (ItemStackHelper.areItemStacksEqual(sortingInventory.getStackInSlot(i), itemStack))
                {
                    outResult.passes = true;
                    return;
                }
            }
        }
    }

    @Override
    public IGuiWidgetWrapped getGuiWidget(int x, int y, int width, int height)
    {
        return new GuiSameItemFilter(x, y, width, height);
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
        return 98;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconY()
    {
        return 238;
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
}
