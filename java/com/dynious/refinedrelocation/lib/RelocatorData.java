package com.dynious.refinedrelocation.lib;

import codechicken.lib.vec.Cuboid6;

public class RelocatorData
{
    public static final Cuboid6 middleCuboid;
    public static final Cuboid6[] sideCuboids = new Cuboid6[6];

    static
    {
        middleCuboid = new Cuboid6(0.2D, 0.2D, 0.2D, 0.8D, 0.8D, 0.8D);

        sideCuboids[0] = new Cuboid6(0.2D, 0.0D, 0.2D, 0.8D, 0.25D, 0.8D);
        sideCuboids[1] = new Cuboid6(0.2D, 0.75D, 0.2D, 0.8D, 1.0D, 0.8D);
        sideCuboids[2] = new Cuboid6(0.2D, 0.2D, 0.0D, 0.8D, 0.8D, 0.25D);
        sideCuboids[3] = new Cuboid6(0.2D, 0.2D, 0.75D, 0.8D, 0.8D, 1.0D);
        sideCuboids[4] = new Cuboid6(0.0D, 0.2D, 0.2D, 0.25D, 0.8D, 0.8D);
        sideCuboids[5] = new Cuboid6(0.75D, 0.2D, 0.2D, 1.0D, 0.8D, 0.8D);
    }
}
