package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.until.Vector3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class TileRelocationPortal extends TileEntity
{
    public int oldId = 0;
    public int oldMeta = 0;
    private Vector3 linkedPos;
    private byte time = 0;

    public void init(int oldId, int oldMeta, Vector3 linkedPos)
    {
        this.oldId = oldId;
        this.oldMeta = oldMeta;
        this.linkedPos = linkedPos;
    }

    public void returnToOldBlock()
    {
        worldObj.setBlock(xCoord, yCoord, zCoord, oldId, oldMeta, 3);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateEntity()
    {
        super.updateEntity();

        time++;
        if (time > 60)
        {
            returnToOldBlock();
        }
        if (linkedPos != null)
        {
            List<EntityLivingBase> entityList = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1));

            for (EntityLivingBase entity : entityList)
            {
                if (entity.posY % 1 < 0.5D)
                {
                    teleportEntity(worldObj, linkedPos.getX(), linkedPos.getY() + 1, linkedPos.getZ(), entity);

                    TileEntity upperTile = worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
                    if (upperTile != null && upperTile instanceof TileRelocationPortal)
                    {
                        ((TileRelocationPortal)upperTile).returnToOldBlock();
                    }
                    upperTile = worldObj.getBlockTileEntity(xCoord, yCoord + 2, zCoord);
                    if (upperTile != null && upperTile instanceof TileRelocationPortal)
                    {
                        ((TileRelocationPortal)upperTile).returnToOldBlock();
                    }
                    returnToOldBlock();
                }
            }
        }
    }

    private void teleportEntity(World world, int x, int y, int z, EntityLivingBase player)
    {
        player.setPositionAndUpdate(x + 0.5, y, z + 0.5);
        player.playSound("mob.endermen.portal", 1.0f, 1.0f);
        for (int particles = 0; particles < 2; particles++) {
            world.spawnParticle("portal", player.posX, player.posY, player.posZ, world.rand.nextGaussian(), world.rand.nextGaussian(), world.rand.nextGaussian());
        }
        returnToOldBlock();
    }

    public boolean isLower()
    {
        return linkedPos != null;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("oldId", oldId);
        compound.setInteger("oldMeta", oldMeta);
        compound.setByte("time", time);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        oldId = compound.getInteger("oldId");
        oldMeta = compound.getInteger("oldMeta");
        time = compound.getByte("time");
    }
}
