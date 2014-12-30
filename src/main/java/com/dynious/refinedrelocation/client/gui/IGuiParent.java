package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.container.ContainerRefinedRelocation;

import java.util.List;

public interface IGuiParent
{
    public void addChild(IGuiWidgetBase child);

    public boolean removeChild(IGuiWidgetBase child);

    public void addChildren(List<IGuiWidgetBase> children);

    public void removeChildren(List<IGuiWidgetBase> children);

    public void clearChildren();

    public ContainerRefinedRelocation getContainer();
}
