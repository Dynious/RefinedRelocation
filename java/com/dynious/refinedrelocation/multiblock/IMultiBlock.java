package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.until.Vector2;
import com.dynious.refinedrelocation.until.Vector3;

import java.util.List;

public interface IMultiBlock
{
    public MultiBlockMap getMultiBlockMap();

    public Vector3 getRelativeLeaderPos();

    public List<Vector2> getOptionalBlockList(int optionalId);
}
