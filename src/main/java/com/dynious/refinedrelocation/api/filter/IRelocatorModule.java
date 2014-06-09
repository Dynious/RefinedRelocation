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
import net.minecraft.util.IIcon;

import java.util.List;

public interface IRelocatorModule
{
    public boolean onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack);

    public void onUpdate(IRelocator relocator, int side);

    public int getOutputSide(IRelocator relocator, int side);

    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IRelocator relocator);

    public Container getContainer(IRelocator relocator);

    public boolean passesFilter(ItemStack stack, boolean input);

    public void readFromNBT(NBTTagCompound compound, IRelocator relocator);

    public void writeToNBT(NBTTagCompound  compound, IRelocator relocator);

    public List<ItemStack> getDrops(IRelocator relocator, int side);

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IRelocator relocator, int side);
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register);
}
