package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.RelocatorModuleBase;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.gui.GuiModuleSneaky;
import com.dynious.refinedrelocation.gui.container.ContainerModuleSneaky;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Resources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleSneaky extends RelocatorModuleBase
{
    private static Icon icon;
    private int side = -1;

    @Override
    public boolean onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        APIUtils.openRelocatorFilterGUI(relocator, player, side);
        return true;
    }

    @Override
    public GuiScreen getGUI(IRelocator relocator)
    {
        return new GuiModuleSneaky(this);
    }

    @Override
    public Container getContainer(IRelocator relocator)
    {
        return new ContainerModuleSneaky(this);
    }

    @Override
    public int getOutputSide(IRelocator relocator, int side)
    {
        return this.side == -1 ? ForgeDirection.OPPOSITES[side] : this.side;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, IRelocator relocator)
    {
        side = compound.getByte("outputSide");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, IRelocator relocator)
    {
        compound.setByte("outputSide", (byte) side);
    }

    @Override
    public List<ItemStack> getDrops(IRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 5));
        return list;
    }

    @Override
    public Icon getIcon(IRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleSneaky");
        System.out.println(icon);
    }

    public int getSide()
    {
        return side;
    }

    public void setSide(int side)
    {
        this.side = side;
    }
}
