package com.dynious.blex.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileBlockExtender extends TileEntity
{
    private int connectedSide;

    public void setConnectedSide(int connectedSide)
    {
        this.connectedSide = connectedSide;
        System.out.println(connectedSide);
    }
}
