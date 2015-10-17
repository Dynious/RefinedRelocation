package com.dynious.refinedrelocation.api.relocator;

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
    void init(IItemRelocator relocator, int side);

    boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack);

    void onUpdate(IItemRelocator relocator, int side);

    ItemStack outputToSide(IItemRelocator relocator, int side, TileEntity inventory, ItemStack stack, boolean simulate);

    boolean isItemDestination();

    ItemStack receiveItemStack(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate);

    void onRedstonePowerChange(boolean isPowered);

    boolean connectsToRedstone();

    int strongRedstonePower(int side);

    @SideOnly(Side.CLIENT)
    GuiScreen getGUI(IItemRelocator relocator, int side, EntityPlayer player);

    Container getContainer(IItemRelocator relocator, int side, EntityPlayer player);

    boolean passesFilter(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate);

    void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound);

    void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound);

    void readClientData(IItemRelocator relocator, int side, NBTTagCompound compound);

    void writeClientData(IItemRelocator relocator, int side, NBTTagCompound compound);

    List<ItemStack> getDrops(IItemRelocator relocator, int side);

    @SideOnly(Side.CLIENT)
    IIcon getIcon(IItemRelocator relocator, int side);

    @SideOnly(Side.CLIENT)
    void registerIcons(IIconRegister register);

    String getDisplayName();

    List<String> getWailaInformation(NBTTagCompound nbtData);

    ItemStack getModuleItemStack();
}
