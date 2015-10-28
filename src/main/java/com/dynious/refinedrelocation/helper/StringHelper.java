package com.dynious.refinedrelocation.helper;

import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public final class StringHelper
{
    public static final String NO = ColorHelper.RED + StatCollector.translateToLocal(Strings.NO);
    public static final String YES = ColorHelper.GREEN + StatCollector.translateToLocal(Strings.YES);

    public static String formatCoordinates(int x, int y, int z)
    {
        return String.format("(%d, %d, %d)", x, y, z);
    }

    public static String getLocalizedDirection(ForgeDirection direction)
    {
        return getLocalizedDirection(direction.ordinal());
    }

    public static String getLocalizedDirection(int direction)
    {
        return StatCollector.translateToLocal(Strings.DIRECTION + direction);
    }

    public static String getLocalizedString(String unlocalizedString)
    {
        return StatCollector.translateToLocal(unlocalizedString);
    }

    public static String getLocalizedBoolean(boolean input)
    {
        if (input)
        {
            return StatCollector.translateToLocal(Strings.TRUE);
        }
        else
        {
            return StatCollector.translateToLocal(Strings.FALSE);
        }
    }
}
