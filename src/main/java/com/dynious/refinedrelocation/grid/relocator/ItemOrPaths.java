package com.dynious.refinedrelocation.grid.relocator;

import java.util.List;

public class ItemOrPaths
{
    public List<PathToRelocator> PATHS;
    public TravellingItem ITEM;

    public ItemOrPaths(List<PathToRelocator> paths)
    {
        this.PATHS = paths;
    }

    public ItemOrPaths(TravellingItem item)
    {
        this.ITEM = item;
    }
}
