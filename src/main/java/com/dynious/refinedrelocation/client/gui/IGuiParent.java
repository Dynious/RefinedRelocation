package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.container.ContainerRefinedRelocation;

import java.util.List;

public interface IGuiParent
{
    void addChild(IGuiWidgetBase child);

    boolean removeChild(IGuiWidgetBase child);

    void addChildren(List<IGuiWidgetBase> children);

    void removeChildren(List<IGuiWidgetBase> children);

    void clearChildren();

    ContainerRefinedRelocation getContainer();

}
