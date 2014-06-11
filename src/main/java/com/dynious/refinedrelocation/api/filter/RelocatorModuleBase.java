package com.dynious.refinedrelocation.api.filter;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.helper.IOHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

public abstract class RelocatorModuleBase implements IRelocatorModule
{
    @Override
    public void init(IRelocator relocator, int side)
    {
    }

    @Override
    public boolean onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        return false;
    }

    @Override
    public void onUpdate(IRelocator relocator, int side)
    {
    }

    @Override
    public ItemStack outputToSide(IRelocator relocator, int side, TileEntity inventory, ItemStack stack, boolean simulate)
    {
        return IOHelper.insert(inventory, stack, ForgeDirection.getOrientation(side).getOpposite(), simulate);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IRelocator relocator, EntityPlayer player)
    {
        return null;
    }

    @Override
    public Container getContainer(IRelocator relocator, EntityPlayer player)
    {
        return null;
    }

    @Override
    public boolean passesFilter(IRelocator relocator, int side, ItemStack stack, boolean input)
    {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
    }

    @Override
    public abstract List<ItemStack> getDrops(IRelocator relocator, int side);

    @Override
    @SideOnly(Side.CLIENT)
    public abstract Icon getIcon(IRelocator relocator, int side);

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register)
    {

    }
}
