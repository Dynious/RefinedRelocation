package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.tileentity.IRelocator;

import java.util.ArrayList;

public class PathToRelocator
{
    public final IRelocator RELOCATOR;
    public final ArrayList<Byte> PATH;

    public PathToRelocator(IRelocator relocator, ArrayList<Byte> path)
    {
        this.RELOCATOR = relocator;
        this.PATH = path;
    }
}
