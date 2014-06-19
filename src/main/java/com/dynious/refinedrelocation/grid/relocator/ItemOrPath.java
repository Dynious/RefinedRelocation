package com.dynious.refinedrelocation.grid.relocator;

import java.util.List;

public class ItemOrPath
{
    public List<PathToRelocator> PATHS;
    public TravellingItem ITEM;

    public ItemOrPath(List<PathToRelocator> paths)
    {
        this.PATHS = paths;
    }

    public ItemOrPath(TravellingItem item)
    {
        this.ITEM = item;
    }
}
