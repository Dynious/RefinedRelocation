package com.dynious.refinedrelocation.api.filter;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IRelocatorFilter
{
    public void onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack);

    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IRelocator relocator);

    public Container getContainer(IRelocator relocator);

    public boolean passesFilter(ItemStack stack);

    public void readFromNBT(NBTTagCompound compound);

    public void writeToNBT(NBTTagCompound  compound);
}
