package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.GuiModularFiltered;

public class GuiButtonOpenModuleSelection extends GuiButton
{
    private int id;

    public GuiButtonOpenModuleSelection(GuiModularFiltered parent, int x, int y, int id)
    {
        super(parent, x, y, 24, 20, 202, 0, null);
        this.id = id;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isMouseInsideBounds(mouseX, mouseY) && (type == 0 || type == 1))
        {
            ((GuiModularFiltered) parent).onListButtonClicked(this);
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    public int getId()
    {
        return id;
    }
}
