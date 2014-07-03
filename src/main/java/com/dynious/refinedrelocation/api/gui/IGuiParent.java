package com.dynious.refinedrelocation.api.gui;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetBase;

import java.util.List;

public interface IGuiParent
{
    public void addChild(IGuiWidgetBase child);

    public boolean removeChild(IGuiWidgetBase child);

    public void addChildren(List<IGuiWidgetBase> children);

    public void removeChildren(List<IGuiWidgetBase> children);

    public void clearChildren();
}
