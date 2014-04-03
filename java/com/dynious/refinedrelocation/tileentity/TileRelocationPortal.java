package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.helper.TeleportHelper;
import com.dynious.refinedrelocation.util.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class TileRelocationPortal extends TileEntity
{
    public Block oldBlock = Blocks.air;
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
            oldBlock = Blocks.air;
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

            if (entityList.size() >= 1)
            {
                EntityLivingBase entity = entityList.get(0);

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
                    ((TileRelocationPortal) upperTile).returnToOldBlock();
                }
                upperTile = worldObj.getTileEntity(xCoord, yCoord + 2, zCoord);
                if (upperTile != null && upperTile instanceof TileRelocationPortal)
                {
                    ((TileRelocationPortal) upperTile).returnToOldBlock();
                }
                returnToOldBlock();
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
