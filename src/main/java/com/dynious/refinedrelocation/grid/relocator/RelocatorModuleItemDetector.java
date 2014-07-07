package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.grid.FilterStandard;
import com.dynious.refinedrelocation.gui.GuiFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerFiltered;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.Arrays;
import java.util.List;

public class RelocatorModuleItemDetector extends RelocatorModuleBase
{
    private static IIcon[] icons = new IIcon[4];
    private static boolean emitRedstoneSignal = false;
    private FilterStandard filter;
    private int tick = 0;

    @Override
    public void init(IItemRelocator relocator, int side)
    {
        filter = new FilterStandard(getFilterTile(this, relocator));
    }

    @Override
    public boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        APIUtils.openRelocatorModuleGUI(relocator, player, side);
        return true;
    }

    @Override
    public void onUpdate(IItemRelocator relocator, int side)
    {
        if (tick > 0)
        {
            tick--;
            if (tick == 0)
            {
                emitRedstoneSignal = false;
                markRedstoneUpdate(relocator.getTileEntity());
            }
        }
    }

    @Override
    public int strongRedstonePower(int side)
    {
        return emitRedstoneSignal ? 15 : 0;
    }

    @Override
    public boolean connectsToRedstone()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new GuiFiltered(getFilterTile(this, relocator));
    }

    @Override
    public Container getContainer(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new ContainerFiltered(getFilterTile(this, relocator));
    }

    @Override
    public boolean passesFilter(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate)
    {
        if (!simulate && stack != null && filter.passesFilter(stack))
        {
            tick = 2;
            emitRedstoneSignal = true;
            markRedstoneUpdate(relocator.getTileEntity());
        }

        return true;
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        return Arrays.asList(new ItemStack(ModItems.relocatorModule, 1, 9));
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return emitRedstoneSignal? icons[1] : icons[0];
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icons[0] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleItemDetector");
        icons[1] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleItemDetectorDetected");
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 9 + ".name");
    }

    private IFilterTileGUI getFilterTile(final RelocatorModuleItemDetector module, final IItemRelocator relocator)
    {
        return new IFilterTileGUI()
        {
            @Override
            public IFilterGUI getFilter()
            {
                return module.filter;
            }

            @Override
            public TileEntity getTileEntity()
            {
                return relocator.getTileEntity();
            }

            @Override
            public void onFilterChanged()
            {
                markRedstoneUpdate(relocator.getTileEntity());
                relocator.getTileEntity().markDirty();
            }
        };
    }

    private void markRedstoneUpdate(TileEntity relocator)
    {
        TileRelocator.markUpdateAndNotify(relocator.getWorldObj(), relocator.xCoord, relocator.yCoord, relocator.zCoord);
    }
}
