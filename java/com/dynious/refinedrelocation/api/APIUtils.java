package com.dynious.refinedrelocation.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public final class APIUtils
{
    private static Object modInstance;
    private static int guiID = -1;

    public static void openFilteringGUI(EntityPlayer entityPlayer, World world, int x, int y, int z)
    {
        if (modInstance == null)
        {
            try
            {
                Class c = Class.forName("com.dynious.refinedrelocation.RefinedRelocation");
                modInstance = c.getField("instance").get(c);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                return;
            }
        }
        if (guiID == -1)
        {
            try
            {
                Class c = Class.forName("com.dynious.refinedrelocation.lib.GuiIds");
                guiID = (Integer) c.getField("FILTERED").get(c);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                return;
            }
        }
        entityPlayer.openGui(modInstance, guiID, world, x, y, z);
    }
}
