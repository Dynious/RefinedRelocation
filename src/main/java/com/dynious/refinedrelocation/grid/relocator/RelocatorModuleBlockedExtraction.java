package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.Arrays;
import java.util.List;

public class RelocatorModuleBlockedExtraction extends RelocatorModuleExtraction
{
    private static IIcon icon;

    @Override
    public boolean passesFilter(IRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate)
    {
        return input;
    }

    @Override
    public List<ItemStack> getDrops(IRelocator relocator, int side)
    {
        return Arrays.asList(new ItemStack(ModItems.relocatorModule, 1, 4));
    }

    @Override
    public IIcon getIcon(IRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleBlockedExtraction");
    }
}
