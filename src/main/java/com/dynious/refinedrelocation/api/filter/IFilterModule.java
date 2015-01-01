package com.dynious.refinedrelocation.api.filter;

import com.dynious.refinedrelocation.client.gui.IGuiWidgetBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IFilterModule
{
    public String getDisplayName();

    public boolean matchesFilter(ItemStack stack);

    @SideOnly(Side.CLIENT)
    public IGuiWidgetBase getGUI();

    public Container getContainer();

    public void readFromNBT(NBTTagCompound compound);

    public void writeToNBT(NBTTagCompound compound);
}
