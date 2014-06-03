package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IRelocatorModule;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.FilterStandard;
import com.dynious.refinedrelocation.gui.GuiFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerFiltered;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Resources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleFilter extends FilterStandard implements IRelocatorModule
{
    private static IIcon icon;

    @Override
    public boolean onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        APIUtils.openRelocatorFilterGUI(relocator, player, side);
        return true;
    }

    @Override
    public void onUpdate(IRelocator relocator, int side)
    {
        //NO-OP
    }

    @Override
    @SideOnly(Side.CLIENT)
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

    @Override
    public List<ItemStack> getDrops(IRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 1));
        return list;
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

    @Override
    public IIcon getIcon(IRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleFilter");
    }
}