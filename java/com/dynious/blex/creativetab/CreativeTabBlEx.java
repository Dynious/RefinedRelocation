package com.dynious.blex.creativetab;

import com.dynious.blex.lib.BlockIds;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;

public class CreativeTabBlEx extends CreativeTabs
{

    public CreativeTabBlEx(int par1, String par2Str)
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
