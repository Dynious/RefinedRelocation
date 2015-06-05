package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;

public interface IContainerFiltered {
    void setBlackList(boolean value);
    void setPriority(int priority);
    IFilterGUI getFilter();
}
