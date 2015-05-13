package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.client.gui.GuiFiltered;
import com.dynious.refinedrelocation.container.ContainerFiltered;
import com.dynious.refinedrelocation.grid.MultiFilter;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleFilter extends RelocatorModuleBase
{
    private static IIcon icon;
    private MultiFilter filter;
    private MultiFilter filterWaila;
    private long lastChange = -401;
    private IItemRelocator relocator;

    @Override
    public void init(IItemRelocator relocator, int side)
    {
        filter = new MultiFilter(getFilterTile(this, relocator));
        this.relocator = relocator;
    }

    @Override
    public boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        APIUtils.openRelocatorModuleGUI(relocator, player, side);
        return true;
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 1 + ".name");
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
        return (!simulate && (relocator.getTileEntity().getWorldObj().getTotalWorldTime() - lastChange) > 400) || filter.passesFilter(stack);
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 1));
        return list;
    }

    private IFilterTileGUI getFilterTile(final RelocatorModuleFilter module, final IItemRelocator relocator)
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
                lastChange = relocator.getTileEntity().getWorldObj().getTotalWorldTime();
                relocator.getTileEntity().markDirty();
            }
        };
    }

    @Override
    public void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        filter.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        filter.writeToNBT(compound);
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleFilter");
    }

    @Override
    public List<String> getWailaInformation(NBTTagCompound nbtData)
    {
        if (filterWaila == null)
        {
            filterWaila = new MultiFilter(getFilterTile(this, relocator));
        }
        List<String> information = new ArrayList<String>();
        filterWaila.readFromNBT(nbtData);
        information.addAll(filterWaila.getWAILAInformation(nbtData));
        return information;
    }
}
