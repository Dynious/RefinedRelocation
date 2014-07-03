package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetBase;
import com.dynious.refinedrelocation.api.gui.IGuiParent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GuiWidgetBase extends Gui implements IGuiWidgetBase
{
    public int x;
    public int y;
    public int w;
    public int h;
    protected List<IGuiWidgetBase> children = new ArrayList<IGuiWidgetBase>();
    protected IGuiParent parent = null;
    protected Minecraft mc;
    protected boolean visible = true;
    protected String tooltipString = null;

    public GuiWidgetBase(int x, int y, int w, int h)
    {
        this.mc = Minecraft.getMinecraft();
        this.setPos(x, y);
        this.setSize(w, h);
        this.setParent(null);
    }

    public GuiWidgetBase(IGuiParent parent)
    {
        this.mc = Minecraft.getMinecraft();
        this.setPos(0, 0);
        this.setSize(50, 50);
        this.setParent(parent);
    }

    public GuiWidgetBase(IGuiParent parent, int x, int y, int w, int h)
    {
        this.mc = Minecraft.getMinecraft();
        this.setPos(x, y);
        this.setSize(w, h);
        this.setParent(parent);
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
    public void addChild(IGuiWidgetBase child)
    {
        this.children.add(child);
    }

    @Override
    public void addChildren(List<IGuiWidgetBase> children)
    {
        this.children.addAll(children);
    }

    @Override
    public void clearChildren()
    {
        this.children.clear();
    }

    @Override
    public boolean removeChild(IGuiWidgetBase child)
    {
        return this.children.remove(child);
    }

    @Override
    public void removeChildren(List<IGuiWidgetBase> children)
    {
        this.children.removeAll(children);
    }

    @Override
    public int getWidth()
    {
        return w;
    }

    @Override
    public int getHeight()
    {
        return h;
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }

    @Override
    public void setPos(int x, int y)
    {
        int dX = x - this.x;
        int dY = y - this.y;
        for (IGuiWidgetBase child : children)
        {
            child.moveX(dX);
            child.moveY(dY);
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public void moveX(int amount)
    {
        x += amount;
        for (IGuiWidgetBase child : children)
        {
            child.moveX(amount);
        }
    }

    @Override
    public void moveY(int amount)
    {
        y += amount;
        for (IGuiWidgetBase child : children)
        {
            child.moveY(amount);
        }
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
        for (IGuiWidgetBase child : this.children)
            tooltip.addAll(child.getTooltip(mouseX, mouseY));
        return tooltip;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        if (!isVisible())
            return;

        for (IGuiWidgetBase child : this.children)
            child.drawBackground(mouseX, mouseY);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (!isVisible())
            return;

        for (IGuiWidgetBase child : this.children)
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
        for (IGuiWidgetBase child : this.children)
            child.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    @Override
    public boolean keyTyped(char c, int i)
    {
        for (IGuiWidgetBase child : this.children)
        {
            if (child.keyTyped(c, i))
                return true;
        }
        return false;
    }

    @Override
    public void handleMouseInput()
    {
        for (IGuiWidgetBase child : this.children)
            child.handleMouseInput();
    }

    @Override
    public void update()
    {
        for (IGuiWidgetBase child : this.children)
            child.update();
    }

    @Override
    public void mouseMovedOrUp(int par1, int par2, int par3)
    {
        for (IGuiWidgetBase child : this.children)
            child.mouseMovedOrUp(par1, par2, par3);
    }
}
