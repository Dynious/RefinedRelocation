package com.dynious.blex.gui;

import com.dynious.blex.gui.widget.IGuiBlExWidgetBase;

import java.util.List;

public interface IGuiParent
{
    public void addChild(IGuiBlExWidgetBase child);

    public boolean removeChild(IGuiBlExWidgetBase child);

    public void addChildren(List<IGuiBlExWidgetBase> children);

    public void removeChildren(List<IGuiBlExWidgetBase> children);

    public void clearChildren();
}
