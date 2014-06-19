package com.dynious.refinedrelocation.tileentity;

import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface ILoopable
{
    public List<TileEntity> getConnectedTiles();
}
