package com.dynious.refinedrelocation.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraft.server.MinecraftServer;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;


public class TeleportHelper
{
    public static void travelToDimension(Entity entity, int dimensionId, int x, int y, int z)
    {
        if (!entity.worldObj.isRemote && !entity.isDead)
        {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            WorldServer newWorldServer = minecraftserver.worldServerForDimension(dimensionId);
            if (entity instanceof EntityPlayerMP)
            {
                minecraftserver.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) entity, dimensionId, new CustomTeleporter(newWorldServer));
                ((EntityPlayerMP) entity).setPositionAndUpdate(x + 0.5, y, z + 0.5);
            }
            else
            {
                travelEntityToDimension(entity, dimensionId, x, y, z);
            }
        }
    }

    public static void travelToPosition(Entity entity, int x, int y, int z)
    {
        if (!entity.worldObj.isRemote)
            entity.setLocationAndAngles(x + 0.5, y, z + 0.5, entity.rotationYaw, entity.rotationPitch);
    }

    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public static void travelEntityToDimension(Entity entity, int dimensionId, int x, int y, int z)
    {
        if (!entity.worldObj.isRemote && !entity.isDead)
        {
            entity.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            int j = entity.dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(j);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionId);
            entity.dimension = dimensionId;

            if (j == 1 && dimensionId == 1)
            {
                worldserver1 = minecraftserver.worldServerForDimension(0);
                entity.dimension = 0;
            }

            entity.worldObj.removeEntity(entity);
            entity.isDead = false;
            entity.worldObj.theProfiler.startSection("reposition");
            minecraftserver.getConfigurationManager().transferEntityToWorld(entity, j, worldserver, worldserver1);
            entity.worldObj.theProfiler.endStartSection("reloading");
            Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);

            if (newEntity != null)
            {
                newEntity.copyDataFrom(entity, true);

                if (j == 1 && dimensionId == 1)
                {
                    ChunkCoordinates chunkcoordinates = worldserver1.getSpawnPoint();
                    chunkcoordinates.posY = entity.worldObj.getTopSolidOrLiquidBlock(chunkcoordinates.posX, chunkcoordinates.posZ);
                    newEntity.setLocationAndAngles((double) chunkcoordinates.posX, (double) chunkcoordinates.posY, (double) chunkcoordinates.posZ, newEntity.rotationYaw, newEntity.rotationPitch);
                }

                worldserver1.spawnEntityInWorld(newEntity);
                newEntity.setPosition(x + 0.5D, y, z + 0.5D);
            }

            entity.isDead = true;
            entity.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            entity.worldObj.theProfiler.endSection();
        }
    }
}
