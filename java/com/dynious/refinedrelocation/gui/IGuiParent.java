package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.gui.widget.IGuiRefinedRelocationWidgetBase;

import java.util.List;

public interface IGuiParent
{
    public void addChild(IGuiRefinedRelocationWidgetBase child);

    public boolean removeChild(IGuiRefinedRelocationWidgetBase child);

    public void addChildren(List<IGuiRefinedRelocationWidgetBase> children);

    public void removeChildren(List<IGuiRefinedRelocationWidgetBase> children);

    public void clearChildren();
}
