package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;

public class GuiButtonFilterType extends GuiButton {

    private final int typeId;

    public GuiButtonFilterType(IGuiParent parent, int x, int y, String labelText, int typeId) {
        super(parent, x, y, 24, 20, 0, 0, labelText);
        this.typeId = typeId;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        if(isMouseInsideBounds(mouseX, mouseY)) {

        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }
}
