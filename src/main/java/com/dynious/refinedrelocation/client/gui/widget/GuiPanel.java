package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;

public class GuiPanel extends GuiWidgetBase {

    public GuiPanel(IGuiParent parent, int x, int y, int w, int h) {
        super(parent, x, y, w, h);
    }

    public void removeAllChildren() {
        children.clear();
    }
}
