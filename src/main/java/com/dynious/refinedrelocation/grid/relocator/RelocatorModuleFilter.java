package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.api.tileentity.INewFilterTile;
import com.dynious.refinedrelocation.client.gui.GuiModularFiltered;
import com.dynious.refinedrelocation.container.ContainerModularFiltered;
import com.dynious.refinedrelocation.grid.Filter;
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

public class RelocatorModuleFilter extends RelocatorModuleBase implements INewFilterTile
{
    private static IIcon icon;
    private Filter newFilter;
    //private FilterStandard filterWaila;
    private long lastChange = -401;
    private IItemRelocator relocator;

    @Override
    public void init(IItemRelocator relocator, int side)
    {
        newFilter = new Filter(this);
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
        return new GuiModularFiltered(this);
    }

    @Override
    public Container getContainer(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new ContainerModularFiltered(this);
    }

    @Override
    public boolean passesFilter(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate)
    {
        return (!simulate && (relocator.getTileEntity().getWorldObj().getTotalWorldTime() - lastChange) > 400) || newFilter.passesFilter(stack);
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 1));
        return list;
    }


    @Override
    public void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        newFilter.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        newFilter.writeToNBT(compound);
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
        /*
        if (filterWaila == null)
        {
            filterWaila = new FilterStandard(getFilterTile(this, relocator));
        }
        List<String> information = new ArrayList<String>();
        filterWaila.readFromNBT(nbtData);
        information.addAll(filterWaila.getWAILAInformation(nbtData));
        */
        return null;
    }

    @Override
    public Filter getFilter()
    {
        return newFilter;
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
        relocator.markUpdate();
    }
}
