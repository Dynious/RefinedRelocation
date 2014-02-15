package com.dynious.blex.gui.widget;

import com.dynious.blex.gui.IGuiParent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GuiBlExWidgetBase extends Gui implements IGuiBlExWidgetBase
{
    public int x;
    public int y;
    public int w;
    public int h;
    protected List<IGuiBlExWidgetBase> children = new ArrayList<IGuiBlExWidgetBase>();
    protected IGuiParent parent = null;
    protected Minecraft mc;
    protected boolean visible = true;
    protected String tooltipString = null;

    public GuiBlExWidgetBase(int x, int y, int w, int h)
    {
        this.setParent(null);
        this.mc = Minecraft.getMinecraft();
        this.setPos(x, y);
        this.setSize(w, h);
    }

    public GuiBlExWidgetBase(IGuiParent parent)
    {
        this.setParent(parent);
        this.mc = Minecraft.getMinecraft();
        this.setPos(0, 0);
        this.setSize(50, 50);
    }

    public GuiBlExWidgetBase(IGuiParent parent, int x, int y, int w, int h)
    {
        this.setParent(parent);
        this.mc = Minecraft.getMinecraft();
        this.setPos(x, y);
        this.setSize(w, h);
    }

    public boolean isMouseInsideBounds(int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    @Override
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    @Override
    public boolean isVisible()
    {
        return this.visible;
    }

    @Override
    public void setParent(IGuiParent parent)
    {
        if (this.parent != null)
            this.parent.removeChild(this);
        if (parent != null)
            parent.addChild(this);

        this.parent = parent;
    }

    @Override
    public IGuiParent getParent()
    {
        return this.parent;
    }

    @Override
    public void addChild(IGuiBlExWidgetBase child)
    {
        this.children.add(child);
    }

    @Override
    public void addChildren(List<IGuiBlExWidgetBase> children)
    {
        this.children.addAll(children);
    }

    @Override
    public void clearChildren()
    {
        this.children.clear();
    }

    @Override
    public boolean removeChild(IGuiBlExWidgetBase child)
    {
        return this.children.remove(child);
    }

    @Override
    public void removeChildren(List<IGuiBlExWidgetBase> children)
    {
        this.children.removeAll(children);
    }

    @Override
    public void setPos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setSize(int w, int h)
    {
        this.w = w;
        this.h = h;
    }

    public void setTooltipString(String tooltipString)
    {
        this.tooltipString = tooltipString;
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = new ArrayList<String>();
        if (this.tooltipString != null && isMouseInsideBounds(mouseX, mouseY))
        {
            tooltip.addAll(Arrays.asList(tooltipString.split("\n")));
        }
        for (IGuiBlExWidgetBase child : this.children)
            tooltip.addAll(child.getTooltip(mouseX, mouseY));
        return tooltip;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        if (!isVisible())
            return;

        for (IGuiBlExWidgetBase child : this.children)
            child.drawBackground(mouseX, mouseY);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (!isVisible())
            return;

        for (IGuiBlExWidgetBase child : this.children)
            child.drawForeground(mouseX, mouseY);
    }

    @Override
    public void draw(int mouseX, int mouseY)
    {
        if (!isVisible())
            return;

        drawBackground(mouseX, mouseY);
        drawForeground(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        for (IGuiBlExWidgetBase child : this.children)
            child.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    @Override
    public boolean keyTyped(char c, int i)
    {
        for (IGuiBlExWidgetBase child : this.children)
        {
            if (child.keyTyped(c, i))
                return true;
        }
        return false;
    }

    @Override
    public void handleMouseInput()
    {
        for (IGuiBlExWidgetBase child : this.children)
            child.handleMouseInput();
    }

    @Override
    public void update()
    {
        for (IGuiBlExWidgetBase child : this.children)
            child.update();
    }
}
