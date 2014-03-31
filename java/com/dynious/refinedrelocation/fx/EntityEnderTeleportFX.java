package com.dynious.refinedrelocation.fx;

import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.world.World;

public class EntityEnderTeleportFX extends EntityPortalFX
{
    public EntityEnderTeleportFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        super(par1World, par2, par4, par6, par8, par10, par12);
        this.particleMaxAge = (int)(Math.random() * 10.0D) + 20;
    }
}
