package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleOneWay extends RelocatorModuleBase
{
    private static IIcon icon0;
    private static IIcon icon1;

    private boolean inputAllowed = true;

    @Override
    public boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        inputAllowed = !inputAllowed;
        return true;
    }

    @Override
    public boolean passesFilter(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate)
    {
        return inputAllowed == input;
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 2 + ".name");
    }

    @Override
    public void onRedstonePowerChange(boolean isPowered)
    {
        if (isPowered)
        {
            inputAllowed = !inputAllowed;
        }
    }

    @Override
    public boolean connectsToRedstone()
    {
        return true;
    }

    @Override
    public void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        inputAllowed = compound.getBoolean("inputAllowed");
    }

    @Override
    public void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        compound.setBoolean("inputAllowed", inputAllowed);
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 2));
        return list;
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return inputAllowed ? icon1 : icon0;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon0 = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleOneWay0");
        icon1 = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleOneWay1");
    }
}
