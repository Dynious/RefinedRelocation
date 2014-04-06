package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.helper.LogHelper;
import com.dynious.refinedrelocation.helper.TeleportHelper;
import com.dynious.refinedrelocation.util.Vector3;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.List;

public class TileRelocationPortal extends TileEntity
{
    public int oldId = 0;
    public int oldMeta = 0;
    private Vector3 linkedPos;
    private int dimension = Integer.MAX_VALUE;
    private byte time = 0;

    /**
     * Used to keep world loaded when portal is open
     */
    private World linkedWorld;

    public void init(int oldId, int oldMeta)
    {
        this.oldId = oldId;
        this.oldMeta = oldMeta;
    }

    public void init(int oldId, int oldMeta, Vector3 linkedPos)
    {
        this.oldId = oldId;
        this.oldMeta = oldMeta;
        this.linkedPos = linkedPos;
    }

    public void init(int oldId, int oldMeta, Vector3 linkedPos, int dimension)
    {
        this.oldId = oldId;
        this.oldMeta = oldMeta;
        this.linkedPos = linkedPos;
        this.dimension = dimension;
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            linkedWorld = MinecraftServer.getServer().worldServerForDimension(dimension);
            Chunk chunk = linkedWorld.getChunkFromBlockCoords(linkedPos.getX(), linkedPos.getZ());
            ForgeChunkManager.forceChunk(ForgeChunkManager.requestTicket(RefinedRelocation.instance, linkedWorld, ForgeChunkManager.Type.NORMAL), new ChunkCoordIntPair(chunk.xPosition, chunk.zPosition));
            LogHelper.info("Force-loaded: " + linkedWorld.provider.getDimensionName());
        }
    }

    public void returnToOldBlock()
    {
        if (dimension != Integer.MAX_VALUE)
        {
            linkedWorld = MinecraftServer.getServer().worldServerForDimension(dimension);
            Chunk chunk = linkedWorld.getChunkFromBlockCoords(linkedPos.getX(), linkedPos.getZ());
            ForgeChunkManager.unforceChunk(ForgeChunkManager.requestTicket(RefinedRelocation.instance, linkedWorld, ForgeChunkManager.Type.NORMAL), new ChunkCoordIntPair(chunk.xPosition, chunk.zPosition));
            LogHelper.info("Stopped force-load for: " + linkedWorld.provider.getDimensionName());
        }
        if (oldId == ModBlocks.relocationPortal.blockID)
        {
            oldId = 0;
            oldMeta = 0;
        }
        worldObj.setBlock(xCoord, yCoord, zCoord, oldId, oldMeta, 3);
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

                TileEntity upperTile = worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
                if (upperTile != null && upperTile instanceof TileRelocationPortal)
                {
                    ((TileRelocationPortal) upperTile).returnToOldBlock();
                }
                upperTile = worldObj.getBlockTileEntity(xCoord, yCoord + 2, zCoord);
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
        compound.setInteger("oldId", oldId);
        compound.setInteger("oldMeta", oldMeta);
        compound.setInteger("dimension", dimension);
        compound.setByte("time", time);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        oldId = compound.getInteger("oldId");
        oldMeta = compound.getInteger("oldMeta");
        dimension = compound.getInteger("dimension");
        time = compound.getByte("time");
    }
}
