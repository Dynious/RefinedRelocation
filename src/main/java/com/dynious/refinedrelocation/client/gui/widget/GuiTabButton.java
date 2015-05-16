package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.IGuiWidgetBase;
import com.dynious.refinedrelocation.lib.Resources;
import org.lwjgl.opengl.GL11;

public class GuiTabButton extends GuiWidgetBase {

    private final GuiTabPanel panel;
    private final IGuiWidgetBase page;
    private boolean active;

    public GuiTabButton(IGuiParent parent, GuiTabPanel panel, int x, int y, IGuiWidgetBase page) {
        super(parent, x, y, 31, 26);
        this.panel = panel;
        this.page = page;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
        if(isInsideBounds(mouseX, mouseY)) {
            setActive(true);
        }
    }

    public void setActive(boolean active) {
        if(this.active == active) {
            return;
        }
        this.active = active;
        if(active) {
            panel.setCurrentPage(page);
            panel.setActiveTabButton(this);
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_SHARED);
        this.drawTexturedModalRect(x + (active ? 2 : 0), y, active ? 0 : 32, 230, w, h);
    }
}
