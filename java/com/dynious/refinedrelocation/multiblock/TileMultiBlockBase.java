package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.until.Vector2;
import com.dynious.refinedrelocation.until.Vector3;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TileMultiBlockBase extends TileEntity implements IMultiBlockLeader
{
    private int checkCount = 0;
    private boolean isFormed = false;
    protected Map<Integer, Integer> typeIds = new HashMap<Integer, Integer>();

    @Override
    public void updateEntity()
    {
        if (!worldObj.isRemote && shouldAutoCheckFormation())
        {
            if (checkCount <= 0)
            {
                checkCount = 200;
                checkMultiBlock();
            }
            checkCount--;
        }
    }

    private void checkMultiBlock()
    {
        IMultiBlock multiBlock = MultiBlockRegistry.getMultiBlock(getMultiBlockIdentifier());
        Vector3 leaderPos = multiBlock.getRelativeLeaderPos();

        for (int x = 0; x < multiBlock.getMultiBlockMap().getSizeX(); x++)
        {
            for (int y = 0; y < multiBlock.getMultiBlockMap().getSizeY(); y++)
            {
                for (int z = 0; z < multiBlock.getMultiBlockMap().getSizeZ(); z++)
                {
                    Vector2 blockInfo = multiBlock.getMultiBlockMap().getBlockAtPos(x, y, z);

                    if (blockInfo != null)
                    {
                        if (blockInfo.getBlock() < 0)
                        {
                            List<Vector2> list = multiBlock.getOptionalBlockList(blockInfo.getBlock());
                            boolean foundBlock = false;
                            for (int i = 0; i < list.size(); i++)
                            {
                                if (checkFormation(list.get(i), xCoord + x - leaderPos.getX(), yCoord + y - leaderPos.getY(), zCoord + z - leaderPos.getZ()))
                                {
                                    foundBlock = true;
                                    typeIds.put(-blockInfo.getBlock(), i);
                                }
                            }
                            if (!foundBlock)
                            {
                                if (isFormed)
                                {
                                    isFormed = false;
                                    onFormationChange();
                                }
                                return;
                            }
                        }
                        else
                        {
                            if (!checkFormation(blockInfo, xCoord + x - leaderPos.getX(), yCoord + y - leaderPos.getY(), zCoord + z - leaderPos.getZ()))
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
        }
        //Init
        if (!isFormed)
        {
            isFormed = true;
            onFormationChange();
        }
    }

    private boolean checkFormation(Vector2 blockInfo, int x, int y, int z)
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

    }

    public void forceCheck()
    {
        checkMultiBlock();
    }
}
