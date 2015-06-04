package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.IGuiWidgetBase;
import com.dynious.refinedrelocation.lib.Resources;
import org.lwjgl.opengl.GL11;

public class GuiTabButton extends GuiWidgetBase {

    private final GuiTabPanel panel;
    private final int tabIndex;
    private final IGuiWidgetBase page;
    private int backgroundTextureX;
    private int backgroundTextureY;
    private final int iconTextureX;
    private final int iconTextureY;
    private boolean active;

    public GuiTabButton(IGuiParent parent, GuiTabPanel panel, int x, int y, IGuiWidgetBase page, int tabIndex, int iconTextureX, int iconTextureY) {
        super(parent, x, y, 31, 26);
        this.panel = panel;
        this.page = page;
        this.tabIndex = tabIndex;
        this.iconTextureX = iconTextureX;
        this.iconTextureY = iconTextureY;
        backgroundTextureX = 0;
        backgroundTextureY = 230;
    }

    public void setPlainTexture() {
        backgroundTextureY = 204;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
        if(isInsideBounds(mouseX, mouseY)) {
            setActive(true);
        }
    }

    public int getTabIndex() {
        return tabIndex;
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
        this.drawTexturedModalRect(x + (active ? 2 : 0), y, backgroundTextureX  + (active ? 0 : 32), backgroundTextureY, w, h);

        if(iconTextureX != -1) {
            this.drawTexturedModalRect(x + 8 + (active ? 2 : -1), y + 4, iconTextureX, iconTextureY, 18, 18);
        }
    }

    public IGuiWidgetBase getTabPage() {
        return page;
    }
}
