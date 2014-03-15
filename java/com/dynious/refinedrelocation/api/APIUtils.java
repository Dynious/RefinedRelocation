package com.dynious.refinedrelocation.api;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.GuiIds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public final class APIUtils
{
    public static void openFilteringGUI(EntityPlayer entityPlayer, World world, int x, int y, int z)
    {
        entityPlayer.openGui(RefinedRelocation.instance, GuiIds.FILTERED, world, x, y, z);
    }
}
