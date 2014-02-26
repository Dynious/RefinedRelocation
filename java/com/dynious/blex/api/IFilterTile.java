package com.dynious.blex.api;

import com.dynious.blex.config.Filter;

public interface IFilterTile
{
    public Filter getFilter();

    public boolean getBlackList();

    public void setBlackList(boolean value);
}
