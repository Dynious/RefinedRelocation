package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.util.BlockAndMeta;
import com.dynious.refinedrelocation.util.MultiBlockAndMeta;
import com.dynious.refinedrelocation.util.Vector3;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TileMultiBlockBase extends TileEntity implements IMultiBlockLeader
{
    public int timer = 0;
    private boolean isFormed = false;
    protected int type = -1;

    @Override
    public void updateEntity()
    {
        timer++;
        if (shouldAutoCheckFormation())
        {
            if (timer % 200 == 0)
            {
                checkMultiBlock();
            }
        }
    }

    private void checkMultiBlock()
    {
        if (worldObj.isRemote)
        {
            return;
        }
        IMultiBlock multiBlock = MultiBlockRegistry.getMultiBlock(getMultiBlockIdentifier());
        Vector3 leaderPos = multiBlock.getRelativeLeaderPos();

        for (int x = 0; x < multiBlock.getMultiBlockMap().getSizeX(); x++)
        {
            for (int y = 0; y < multiBlock.getMultiBlockMap().getSizeY(); y++)
            {
                for (int z = 0; z < multiBlock.getMultiBlockMap().getSizeZ(); z++)
                {
                    Object blockInfo = multiBlock.getMultiBlockMap().getBlockAndMetaAtPos(x, y, z);

                    if (blockInfo instanceof MultiBlockAndMeta)
                    {
                        MultiBlockAndMeta multiBlockAndMeta = (MultiBlockAndMeta) blockInfo;
                        boolean foundBlock = false;
                        for (int i = 0; i < multiBlockAndMeta.getBlockAndMetas().size(); i++)
                        {
                            if (checkFormation(multiBlockAndMeta.getBlockAndMetas().get(i), xCoord + x - leaderPos.getX(), yCoord + y - leaderPos.getY(), zCoord + z - leaderPos.getZ()))
                            {
                                foundBlock = true;
                                type = i;
                                break;
                            }
                        }
                        if (!foundBlock)
                        {
                            //UnInit
                            if (isFormed)
                            {
                                isFormed = false;
                                onFormationChange();
                            }
                            return;
                        }
                    }
                    else if (blockInfo instanceof BlockAndMeta)
                    {
                        BlockAndMeta blockAndMeta = (BlockAndMeta) blockInfo;
                        if (!checkFormation(blockAndMeta, xCoord + x - leaderPos.getX(), yCoord + y - leaderPos.getY(), zCoord + z - leaderPos.getZ()))
                        {
                            //UnInit
                            if (isFormed)
                            {
                                isFormed = false;
                                onFormationChange();
                            }
                            return;
                        }
                    }
                }
            }
        }
        //Init
        if (!isFormed)
        {
            isFormed = true;
            onFormationChange();
        }
    }

    private boolean checkFormation(BlockAndMeta blockInfo, int x, int y, int z)
    {
        if (blockInfo.getBlockId() == 0)
        {
            if (!worldObj.isAirBlock(x, y, z))
            {
                return false;
            }
        }
        else if (blockInfo.getMeta() == -1)
        {
            int id = worldObj.getBlockId(x, y, z);
            if (id != blockInfo.getBlockId())
            {
                return false;
            }
        }
        else
        {
            int id = worldObj.getBlockId(x, y, z);
            int meta = worldObj.getBlockMetadata(x, y, z);
            if (id != blockInfo.getBlockId() || meta != blockInfo.getMeta())
            {
                return false;
            }
        }
        return true;
    }

    public boolean shouldAutoCheckFormation()
    {
        return true;
    }

    public boolean isFormed(boolean forceRecheck)
    {
        if (forceRecheck)
        {
            forceCheck();
        }
        return isFormed;
    }

    protected void onFormationChange()
    {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void forceCheck()
    {
        checkMultiBlock();
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("isFormed", isFormed);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
    {
        isFormed = packet.data.getBoolean("isFormed");
    }
}
