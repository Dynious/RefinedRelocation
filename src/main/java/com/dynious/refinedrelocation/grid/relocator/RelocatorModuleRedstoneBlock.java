package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.filter.RelocatorModuleBase;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
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
    public boolean passesFilter(IRelocator relocator, int side, ItemStack stack, boolean input)
    {
        return !relocator.getRedstoneState();
    }

    @Override
    public List<ItemStack> getDrops(IRelocator relocator, int side)
    {
        return Arrays.asList(new ItemStack(ModItems.relocatorModule, 1, 7));
    }

    @Override
    public IIcon getIcon(IRelocator relocator, int side)
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
