package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import java.util.Arrays;
import java.util.List;

public class RelocatorModuleRedstoneBlock extends RelocatorModuleBase
{
    private static Icon iconOn;
    private static Icon iconOff;

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
    public Icon getIcon(IItemRelocator relocator, int side)
    {
        return relocator.getRedstoneState() ? iconOn : iconOff;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        iconOn = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleRedstoneBlockOn");
        iconOff = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleRedstoneBlockOff");
    }
}
