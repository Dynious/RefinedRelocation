package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.util.BlockAndMeta;
import com.dynious.refinedrelocation.util.MultiBlockAndMeta;
import com.dynious.refinedrelocation.util.Vector3;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
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
        if (blockInfo.getBlock() == Blocks.air)
        {
            if (!worldObj.isAirBlock(x, y, z))
            {
                return false;
            }
        }
        else if (blockInfo.getMeta() == -1)
        {
            Block block = worldObj.getBlock(x, y, z);
            if (block != blockInfo.getBlock())
            {
                return false;
            }
        }
        else
        {
            Block block = worldObj.getBlock(x, y, z);
            int meta = worldObj.getBlockMetadata(x, y, z);
            if (block != blockInfo.getBlock() || meta != blockInfo.getMeta())
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
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        isFormed = pkt.func_148857_g().getBoolean("isFormed");
    }
}
