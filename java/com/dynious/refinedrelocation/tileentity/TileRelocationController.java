package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;

public class TileRelocationController extends TileMultiBlockBase
{
    @Override
    public String getMultiBlockIdentifier()
    {
        return Names.playerRelocator;
    }

    @Override
    public boolean shouldAutoCheckFormation()
    {
        return false;
    }

    @Override
    protected void onFormationChange()
    {
    }

    public boolean isIntraLinker()
    {
        return typeIds.get(1) == 1;
    }
}
