package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.graphics.TextureRegion;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.IGuiWidgetBase;
import com.dynious.refinedrelocation.client.gui.SharedAtlas;
import org.lwjgl.opengl.GL11;

public class GuiTabButton extends GuiWidgetBase {

    private final GuiTabPanel panel;
    private final int tabIndex;
    private final IGuiWidgetBase page;
    private final TextureRegion[] textureTab = new TextureRegion[2];
    private final TextureRegion textureIcon;
    private boolean active;

    public GuiTabButton(IGuiParent parent, GuiTabPanel panel, int x, int y, IGuiWidgetBase page, int tabIndex, TextureRegion textureIcon) {
        super(parent, x, y, 31, 26);
        this.panel = panel;
        this.page = page;
        this.tabIndex = tabIndex;
        this.textureIcon = textureIcon;
        textureTab[0] = SharedAtlas.findRegion("tab_inactive_slot");
        textureTab[1] = SharedAtlas.findRegion("tab_active_slot");
    }

    public void setPlainTexture() {
        textureTab[0] = SharedAtlas.findRegion("tab_inactive");
        textureTab[1] = SharedAtlas.findRegion("tab_active");
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
        if (isInsideBounds(mouseX, mouseY)) {
            setActive(true);
        }
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setActive(boolean active) {
        if (this.active == active) {
            return;
        }
        this.active = active;
        if (active) {
            panel.setCurrentPage(page);
            panel.setActiveTabButton(this);
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);

        GL11.glColor4f(1f, 1f, 1f, 1f);
        textureTab[active ? 1 : 0].draw(x + (active ? 2 : 0), y);

        if (textureIcon != null) {
            textureIcon.draw(x + 17 + (active ? 2 : 0) - textureIcon.getRegionWidth() / 2, y + h / 2 - textureIcon.getRegionHeight() / 2);
        }
    }

    public IGuiWidgetBase getTabPage() {
        return page;
    }
}
