package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.helper.TeleportHelper;
import com.dynious.refinedrelocation.until.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class TileRelocationPortal extends TileEntity
{
    public Block oldBlock = null;
    public int oldMeta = 0;
    private Vector3 linkedPos;
    private int dimension = Integer.MAX_VALUE;
    private byte time = 0;

    public void init(Block oldBlock, int oldMeta)
    {
        this.oldBlock = oldBlock;
        this.oldMeta = oldMeta;
    }

    public void init(Block oldBlock, int oldMeta, Vector3 linkedPos)
    {
        this.oldBlock = oldBlock;
        this.oldMeta = oldMeta;
        this.linkedPos = linkedPos;
    }

    public void init(Block oldBlock, int oldMeta, Vector3 linkedPos, int dimension)
    {
        this.oldBlock = oldBlock;
        this.oldMeta = oldMeta;
        this.linkedPos = linkedPos;
        this.dimension = dimension;
    }

    public void returnToOldBlock()
    {
        if (oldBlock == ModBlocks.relocationPortal)
        {
            oldBlock = null;
            oldMeta = 0;
        }
        worldObj.setBlock(xCoord, yCoord, zCoord, oldBlock, oldMeta, 3);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (worldObj.isRemote)
        {
            return;
        }
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
                    if (dimension != Integer.MAX_VALUE && dimension != worldObj.provider.dimensionId)
                    {
                        TeleportHelper.travelToDimension(entity, dimension, linkedPos.getX(), linkedPos.getY() + 1, linkedPos.getZ());
                    }
                    else
                    {
                        TeleportHelper.travelToPosition(entity, linkedPos.getX(), linkedPos.getY() + 1, linkedPos.getZ());
                    }

                    TileEntity upperTile = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
                    if (upperTile != null && upperTile instanceof TileRelocationPortal)
                    {
                        ((TileRelocationPortal)upperTile).returnToOldBlock();
                    }
                    upperTile = worldObj.getTileEntity(xCoord, yCoord + 2, zCoord);
                    if (upperTile != null && upperTile instanceof TileRelocationPortal)
                    {
                        ((TileRelocationPortal)upperTile).returnToOldBlock();
                    }
                    returnToOldBlock();
                    break;
                }
            }
        }
    }

    public boolean isLower()
    {
        return linkedPos != null;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("oldBlock", Block.getIdFromBlock(oldBlock));
        compound.setInteger("oldMeta", oldMeta);
        compound.setInteger("dimension", dimension);
        compound.setByte("time", time);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        oldBlock = Block.getBlockById(compound.getInteger("oldBlock"));
        oldMeta = compound.getInteger("oldMeta");
        dimension = compound.getInteger("dimension");
        time = compound.getByte("time");
    }
}
