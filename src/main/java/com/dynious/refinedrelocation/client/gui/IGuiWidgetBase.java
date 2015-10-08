package com.dynious.refinedrelocation.client.gui;

import java.util.List;

public interface IGuiWidgetBase extends IGuiParent
{
    IGuiParent getParent();

    void setParent(IGuiParent parent);

    int getWidth();

    int getHeight();

    void setSize(int w, int h);

    int getX();

    int getY();

    void setPos(int x, int y);

    void moveX(int amount);

    void moveY(int amount);

    boolean isVisible();

    void setVisible(boolean visible);

    void getTooltip(List<String> tooltip, int mouseX, int mouseY);

    void drawForeground(int mouseX, int mouseY);

    void drawBackground(int mouseX, int mouseY);

    void draw(int mouseX, int mouseY);

    void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown);

    boolean keyTyped(char c, int i);

    void handleMouseInput();

    void update();

    void mouseMovedOrUp(int par1, int par2, int par3);

    boolean isInsideBounds(int x, int y);

    IGuiWidgetBase getChildAt(int mouseX, int mouseY);

    IGuiWidgetBase getWidgetAt(int mouseX, int mouseY);

    List<IGuiWidgetBase> getChildren();

}
