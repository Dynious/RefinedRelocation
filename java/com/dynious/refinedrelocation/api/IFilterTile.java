package com.dynious.refinedrelocation.api;

public interface IFilterTile
{
    public IFilter getFilter();

    public Priority getPriority();

    public static enum Priority
    {
        HIGH,
        NORMAL,
        LOW
    }
}
