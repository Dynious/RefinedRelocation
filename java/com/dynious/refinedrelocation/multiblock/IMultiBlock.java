package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.util.BlockAndMeta;
import com.dynious.refinedrelocation.util.Vector3;

import java.util.List;

public interface IMultiBlock
{
    public MultiBlockMap getMultiBlockMap();

    public Vector3 getRelativeLeaderPos();
}
