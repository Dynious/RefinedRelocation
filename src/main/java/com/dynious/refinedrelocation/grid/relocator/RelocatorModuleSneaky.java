package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.gui.GuiModuleSneaky;
import com.dynious.refinedrelocation.gui.container.ContainerModuleSneaky;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
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
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleSneaky extends RelocatorModuleBase
{
    private static IIcon icon;
    private int side;
    private int outputSide = -1;
    private TileRelocator tileRelocator;

    @Override
    public void init(IItemRelocator relocator, int side)
    {
        this.tileRelocator = (TileRelocator) relocator.getTileEntity();
        this.outputSide = ForgeDirection.OPPOSITES[side];
        this.side = side;
    }

    @Override
    public boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        APIUtils.openRelocatorModuleGUI(relocator, player, side);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new GuiModuleSneaky(this);
    }

    @Override
    public Container getContainer(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new ContainerModuleSneaky(this);
    }

    @Override
    public ItemStack outputToSide(IItemRelocator relocator, int side, TileEntity inventory, ItemStack stack, boolean simulate)
    {
        return IOHelper.insert(inventory, stack, ForgeDirection.getOrientation(this.outputSide), simulate);
    }

    @Override
    public void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        outputSide = compound.getByte("outputSide");
    }

    @Override
    public void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        compound.setByte("outputSide", (byte) outputSide);
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 5));
        return list;
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 5 + ".name");
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleSneaky");
    }

    public int getOutputSide()
    {
        return outputSide;
    }

    public void setOutputSide(int outputSide)
    {
        this.outputSide = outputSide;
    }

    public TileRelocator getRelocator()
    {
        return tileRelocator;
    }

    public ForgeDirection getSide()
    {
        return ForgeDirection.getOrientation(side);
    }
}
