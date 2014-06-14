package com.dynious.refinedrelocation.api.filter;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
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

import java.util.List;

public interface IRelocatorModule
{
    public void init(IRelocator relocator, int side);

    public boolean onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack);

    public void onUpdate(IRelocator relocator, int side);

    public ItemStack outputToSide(IRelocator relocator, int side, TileEntity inventory, ItemStack stack, boolean simulate);

    public void onRedstonePowerChange(boolean isPowered);

    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IRelocator relocator, EntityPlayer player);

    public Container getContainer(IRelocator relocator, EntityPlayer player);

    public boolean passesFilter(IRelocator relocator, int side, ItemStack stack, boolean input);

    public void readFromNBT(NBTTagCompound compound);

    public void writeToNBT(NBTTagCompound  compound);

    public List<ItemStack> getDrops(IRelocator relocator, int side);

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IRelocator relocator, int side);
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register);
}
