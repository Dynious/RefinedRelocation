package com.dynious.blex.gui;

import java.util.List;
import com.dynious.blex.gui.widget.IGuiBlExWidgetBase;

public interface IGuiParent
{
    public void addChild(IGuiBlExWidgetBase child);
    public boolean removeChild(IGuiBlExWidgetBase child);
    public void addChildren(List<IGuiBlExWidgetBase> children);
    public void removeChildren(List<IGuiBlExWidgetBase> children);
    public void clearChildren();
}
