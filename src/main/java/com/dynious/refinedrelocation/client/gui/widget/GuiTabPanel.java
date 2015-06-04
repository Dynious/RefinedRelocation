package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.IGuiWidgetBase;

public class GuiTabPanel extends GuiWidgetBase {

    private IGuiWidgetBase currentPage;
    private GuiTabButton activeTabButton;

    public GuiTabPanel(IGuiParent parent, int x, int y, int w, int h) {
        super(parent, x, y, w, h);
    }

    public void setCurrentPage(IGuiWidgetBase currentPage) {
        if(this.currentPage != null) {
            removeChild(this.currentPage);
        }
        this.currentPage = currentPage;
        currentPage.setParent(this);
    }

    public void setActiveTabButton(GuiTabButton tabButton) {
        if(this.activeTabButton == tabButton) {
            return;
        }
        if(this.activeTabButton != null) {
            this.activeTabButton.setActive(false);
        }
        this.activeTabButton = tabButton;
        this.activeTabButton.setActive(true);
    }

    public GuiTabButton getActiveTabButton() {
        return activeTabButton;
    }
}
