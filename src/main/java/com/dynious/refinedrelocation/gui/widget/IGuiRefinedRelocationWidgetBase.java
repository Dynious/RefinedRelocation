package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.gui.IGuiParent;

import java.util.List;

public interface IGuiRefinedRelocationWidgetBase extends IGuiParent
{
    public void setParent(IGuiParent parent);

    public IGuiParent getParent();

    void setSize(int w, int h);

    void setPos(int x, int y);

    void setVisible(boolean visible);

    boolean isVisible();

    public List<String> getTooltip(int mouseX, int mouseY);

    public void drawForeground(int mouseX, int mouseY);

    public void drawBackground(int mouseX, int mouseY);

    public void draw(int mouseX, int mouseY);

    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown);

    public boolean keyTyped(char c, int i);

    public void handleMouseInput();

    public void update();

    public void mouseMovedOrUp(int par1, int par2, int par3);
}
