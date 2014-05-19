package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IRelocatorModule;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.FilterStandard;
import com.dynious.refinedrelocation.gui.GuiFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerFiltered;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class RelocatorModuleFilter extends FilterStandard implements IRelocatorModule
{
    @Override
    public void onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        APIUtils.openRelocatorFilterGUI(relocator, player, side);
    }

    @Override
    public void onUpdate(IRelocator relocator, int side)
    {
        //NO-OP
    }

    @Override
    public GuiScreen getGUI(IRelocator relocator)
    {
        return new GuiFiltered(getFilterTile(this, relocator));
    }

    @Override
    public Container getContainer(IRelocator relocator)
    {
        return new ContainerFiltered(getFilterTile(this, relocator));
    }

    @Override
    public boolean passesFilter(ItemStack stack, boolean input)
    {
        return passesFilter(stack);
    }

    private IFilterTileGUI getFilterTile(final RelocatorModuleFilter filter, final IRelocator relocator)
    {
        return new IFilterTileGUI()
        {
            @Override
            public IFilterGUI getFilter()
            {
                return filter;
            }

            @Override
            public TileEntity getTileEntity()
            {
                return relocator.getTileEntity();
            }
        };
    }
}