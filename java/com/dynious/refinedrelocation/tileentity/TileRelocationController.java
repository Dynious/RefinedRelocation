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
    protected void onStateChange()
    {
        System.out.println("State change. State: " + isFormed(false));
    }
}
