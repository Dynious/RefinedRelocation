package com.dynious.refinedrelocation.helper;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class CustomTeleporter extends Teleporter
{
    public CustomTeleporter(WorldServer par1WorldServer)
    {
        super(par1WorldServer);
    }

    @Override
    public boolean makePortal(Entity entity)
    {
        return true;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float yaw)
    {
        int newX = MathHelper.floor_double(entity.posX);
        int newY = MathHelper.floor_double(entity.posY) + 1;
        int newZ = MathHelper.floor_double(entity.posZ);

        entity.setLocationAndAngles((double) newX, (double) newY, (double) newZ, entity.rotationYaw, 0.0F);
        entity.motionX = entity.motionY = entity.motionZ = 0.0D;
    }
}
