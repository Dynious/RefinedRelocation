package com.dynious.refinedrelocation.helper;

public class DistanceHelper
{
    public static int getDistanceSq(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        int x = x1 - x2;
        int y = y1 - y2;
        int z = z1 - z2;
        return x * x + y * y + z * z;
    }
}
