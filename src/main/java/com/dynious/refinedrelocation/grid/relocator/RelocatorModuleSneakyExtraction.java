package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.gui.GuiModuleSneaky;
import com.dynious.refinedrelocation.gui.GuiModuleSneakyExtraction;
import com.dynious.refinedrelocation.gui.container.ContainerModuleSneakyExtraction;
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
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleSneakyExtraction extends RelocatorModuleExtraction
{
    private static IIcon icon;
    private int side;
    private int extractionSide = -1;
    private TileRelocator tileRelocator;

    @Override
    protected int getExtractionSide(int side)
    {
        return extractionSide;
    }

    @Override
    public void init(IItemRelocator relocator, int side)
    {
        this.tileRelocator = (TileRelocator) relocator.getTileEntity();
        this.extractionSide = ForgeDirection.OPPOSITES[side];
        this.side = side;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new GuiModuleSneakyExtraction(this);
    }

    @Override
    public Container getContainer(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new ContainerModuleSneakyExtraction(this);
    }

    @Override
    public void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        extractionSide = compound.getByte("extractionSide");
    }

    @Override
    public void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        compound.setByte("extractionSide", (byte) extractionSide);
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 11));
        return list;
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 11 + ".name");
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleSneakyExtraction");
    }

    public int getExtractionSide()
    {
        return extractionSide;
    }

    public void setExtractionSide(int extractionSide)
    {
        lastCheckedSlot = -1;
        this.extractionSide = extractionSide;
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
