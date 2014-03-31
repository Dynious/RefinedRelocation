package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.until.Vector2;
import com.dynious.refinedrelocation.until.Vector3;
import net.minecraft.tileentity.TileEntity;

public abstract class TileMultiBlockBase extends TileEntity implements IMultiBlockLeader
{
    private int checkCount = 0;
    private boolean isFormed = false;

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
        Vector3 vec3 = multiBlock.getRelativeLeaderPos();
        int id, meta;

        for (int x = 0; x < multiBlock.getMultiBlockMap().getSizeX(); x++)
        {
            for (int y = 0; y < multiBlock.getMultiBlockMap().getSizeY(); y++)
            {
                for (int z = 0; z < multiBlock.getMultiBlockMap().getSizeZ(); z++)
                {
                    Vector2 vec = multiBlock.getMultiBlockMap().getBlockAtPos(x, y, z);

                    if (vec != null)
                    {
                        if (vec.getX() == 0)
                        {
                            if (!worldObj.isAirBlock(xCoord + x - vec3.getX(), yCoord + y - vec3.getY(), zCoord + z - vec3.getZ()))
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
                        else if (vec.getY() == -1)
                        {
                            id = worldObj.getBlockId(xCoord + x - vec3.getX(), yCoord + y - vec3.getY(), zCoord + z - vec3.getZ());
                            if (id != vec.getX())
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
                        else
                        {
                            id = worldObj.getBlockId(xCoord + x - vec3.getX(), yCoord + y - vec3.getY(), zCoord + z - vec3.getZ());
                            meta = worldObj.getBlockMetadata(xCoord + x - vec3.getX(), yCoord + y - vec3.getY(), zCoord + z - vec3.getZ());
                            if (id != vec.getX() || meta != vec.getY())
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
