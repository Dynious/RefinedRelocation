package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Resources;
import org.lwjgl.opengl.GL11;

public abstract class GuiTabButton extends GuiWidgetBase {

    private boolean active;

    public GuiTabButton(IGuiParent parent, int x, int y, int w, int h) {
        super(parent, x, y, w, h);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
        if(isMouseInsideBounds(mouseX, mouseY)) {
            active = true;
            onActivated();
        }
    }

    public void setActive(boolean active) {
        this.active = active;
        if(active) {
            onActivated();
        }
    }

    public abstract void onActivated();

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_SHARED);
        this.drawTexturedModalRect(x + (active ? 2 : 0), y, active ? 0 : 32, 230, w, h);
    }
}
