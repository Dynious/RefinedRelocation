package com.dynious.refinedrelocation.creativetab;

import com.dynious.refinedrelocation.lib.BlockIds;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;

public class CreativeTabRefinedRelocation extends CreativeTabs
{

    public CreativeTabRefinedRelocation(int par1, String par2Str)
    {
        super(par1, par2Str);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getTabIconItemIndex()
    {
        return BlockIds.BLOCK_EXTENDER;
    }
}
