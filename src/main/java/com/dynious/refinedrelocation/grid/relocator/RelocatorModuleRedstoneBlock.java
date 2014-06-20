package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.Arrays;
import java.util.List;

public class RelocatorModuleRedstoneBlock extends RelocatorModuleBase
{
    private static IIcon iconOn;
    private static IIcon iconOff;

    @Override
    public boolean passesFilter(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate)
    {
        return !relocator.getRedstoneState();
    }


    @Override
    public boolean connectsToRedstone()
    {
        return true;
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        return Arrays.asList(new ItemStack(ModItems.relocatorModule, 1, 7));
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return relocator.getRedstoneState() ? iconOn : iconOff;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        iconOn = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleRedstoneBlockOn");
        iconOff = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleRedstoneBlockOff");
    }
}
