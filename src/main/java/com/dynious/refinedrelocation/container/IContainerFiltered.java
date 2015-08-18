package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;

public interface IContainerFiltered
{
    void setPriority(int priority);

    IFilterGUI getFilter();
}
