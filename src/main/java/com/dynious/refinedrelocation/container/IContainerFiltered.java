package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.filter.IMultiFilter;

public interface IContainerFiltered {
    void setBlackList(boolean value);
    void setPriority(int priority);
    IMultiFilter getFilter();
}
