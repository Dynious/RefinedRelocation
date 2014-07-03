package com.dynious.refinedrelocation.api.relocator;

import com.dynious.refinedrelocation.tileentity.IRelocator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;

import java.util.List;

public interface IRelocatorModule
{
    public void init(IItemRelocator relocator, int side);

    public boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack);

    public void onUpdate(IItemRelocator relocator, int side);

    public ItemStack outputToSide(IItemRelocator relocator, int side, TileEntity inventory, ItemStack stack, boolean simulate);

    public void onRedstonePowerChange(boolean isPowered);

    public boolean connectsToRedstone();

    public int strongRedstonePower(int side);

    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IItemRelocator relocator, int side, EntityPlayer player);

    public Container getContainer(IItemRelocator relocator, int side, EntityPlayer player);

    public boolean passesFilter(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate);

    public void readFromNBT(NBTTagCompound compound);

    public void writeToNBT(NBTTagCompound  compound);

    public List<ItemStack> getDrops(IItemRelocator relocator, int side);

    @SideOnly(Side.CLIENT)
    public Icon getIcon(IItemRelocator relocator, int side);

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register);
}
