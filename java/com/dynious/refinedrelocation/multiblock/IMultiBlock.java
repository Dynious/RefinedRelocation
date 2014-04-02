package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.until.BlockAndMeta;
import com.dynious.refinedrelocation.until.Vector3;

import java.util.List;

public interface IMultiBlock
{
    public MultiBlockMap getMultiBlockMap();

    public Vector3 getRelativeLeaderPos();

    public List<BlockAndMeta> getOptionalBlockList(int optionalId);
}
