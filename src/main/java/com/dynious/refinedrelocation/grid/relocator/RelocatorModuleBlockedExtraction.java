package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class RelocatorModuleBlockedExtraction extends RelocatorModuleExtraction
{
    private static Icon icon;

    @Override
    public boolean passesFilter(IRelocator relocator, int side, ItemStack stack, boolean input)
    {
        return input;
    }

    @Override
    public Icon getIcon(IRelocator relocator, int side)
    {
        return icon;
    }
    
    @Override
    public void registerIcons(IconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleBlockedExtraction");
    }
}
