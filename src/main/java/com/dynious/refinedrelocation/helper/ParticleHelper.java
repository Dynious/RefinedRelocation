package com.dynious.refinedrelocation.helper;

import net.minecraft.world.World;

public class ParticleHelper
{
    public static void spawnParticlesInCircle(String particle, float circleRadius, int amount, World world, double x, double y, double z, boolean randY)
    {
        double steps = (2*Math.PI)/amount;
        double xPos;
        double zPos;

        for (int i = 0; i < amount; i++)
        {
            xPos = circleRadius*Math.sin(i * steps);
            zPos = circleRadius*Math.cos(i * steps);

            world.spawnParticle(particle, x, y + (randY ? world.rand.nextGaussian() : 0.0F)/2, z, xPos, 1F, zPos);
        }
    }
}
