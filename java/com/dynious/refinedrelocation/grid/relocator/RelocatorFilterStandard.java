package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IRelocatorFilter;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.FilterStandard;
import com.dynious.refinedrelocation.gui.GuiFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerFiltered;
import com.dynious.refinedrelocation.lib.GuiIds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class RelocatorFilterStandard extends FilterStandard implements IRelocatorFilter
{
    @Override
    public void onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        player.openGui(RefinedRelocation.instance, GuiIds.RELOCATOR_FILTER_BASE + side, relocator.getTileEntity().getWorldObj(), relocator.getTileEntity().xCoord, relocator.getTileEntity().yCoord, relocator.getTileEntity().zCoord);
    }

    @Override
    public GuiScreen getGUI(IRelocator relocator)
    {
        return new GuiFiltered(getFilterTile(this));
    }

    @Override
    public Container getContainer(IRelocator relocator)
    {
        return new ContainerFiltered(getFilterTile(this));
    }

    private IFilterTileGUI getFilterTile(final RelocatorFilterStandard r)
    {
        return new IFilterTileGUI()
        {
            @Override
            public IFilterGUI getFilter()
            {
                return r;
            }
        };
    }
}
