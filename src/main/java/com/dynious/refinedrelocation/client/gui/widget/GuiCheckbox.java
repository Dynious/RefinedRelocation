package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.graphics.TextureRegion;
import com.dynious.refinedrelocation.client.gui.GuiRefinedRelocationContainer;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.SharedAtlas;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class GuiCheckbox extends GuiWidgetBase {
    public final int textureW = 10;

    protected boolean checked = false;

    protected GuiLabel label;
    private final TextureRegion[] textureCheckbox = new TextureRegion[2];
    private final TextureRegion[] textureCheckboxChecked = new TextureRegion[2];
    private boolean adventureModeRestriction;

    public GuiCheckbox(IGuiParent parent, int x, int y, int w, int h, String labelText) {
        super(parent, x, y, w, h);

        textureCheckbox[0] = SharedAtlas.findRegion("checkbox");
        textureCheckbox[1] = SharedAtlas.findRegion("checkbox_hover");
        textureCheckboxChecked[0] = SharedAtlas.findRegion("checkbox_checked");
        textureCheckboxChecked[1] = SharedAtlas.findRegion("checkbox_checked_hover");

        label = new GuiLabel(this, x + textureCheckbox[0].getRegionWidth() + 6, y + h / 2 - mc.fontRenderer.FONT_HEIGHT / 2, labelText, 0xffffff, true);
        label.drawCentered = false;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        boolean hovered = isInsideBounds(mouseX, mouseY);
        label.setColor(hovered ? 0xaaffff : 0xffffff);
        if (!hovered) {
            Gui.drawRect(x, y, x + w, y + h, 0x44ffffff);
        }
        if(checked) {
            textureCheckboxChecked[hovered ? 1 : 0].draw(x + 3, y + 1);
        } else {
            textureCheckbox[hovered ? 1 : 0].draw(x + 3, y + 1);
        }
        super.drawBackground(mouseX, mouseY);
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        if (isInsideBounds(mouseX, mouseY) && type == 0) {
            if (!isAdventureModeRestriction() || !GuiRefinedRelocationContainer.isRestrictedAccessWithError()) {
                setChecked(!getChecked());
                onStateChangedByUser(getChecked());
            }
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    protected void onStateChangedByUser(boolean newState) {
    }

    public final boolean isAdventureModeRestriction() {
        return adventureModeRestriction;
    }

    public final void setAdventureModeRestriction(boolean adventureModeRestriction) {
        this.adventureModeRestriction = adventureModeRestriction;
    }
}
