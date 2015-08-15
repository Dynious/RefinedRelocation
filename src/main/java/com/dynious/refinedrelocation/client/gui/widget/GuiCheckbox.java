package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.GuiRefinedRelocationContainer;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class GuiCheckbox extends GuiWidgetBase
{
    public final int textureX = 0;
    public final int textureY = 148;
    public final int textureW = 10;
    public final int textureH = 10;

    protected boolean checked = false;

    protected GuiLabel label;
    private boolean adventureModeRestriction;

    public GuiCheckbox(IGuiParent parent, int x, int y, int w, int h, String labelText)
    {
        super(parent, x, y, w, h);
        label = new GuiLabel(this, x + textureW + 6, y + h / 2 - mc.fontRenderer.FONT_HEIGHT / 2, labelText, 0xffffff, true);
        label.drawCentered = false;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        mc.getTextureManager().bindTexture(Resources.GUI_SHARED);
        GL11.glColor4f(1F, 1F, 1F, 1F);

        boolean hovered = isInsideBounds(mouseX, mouseY);
        label.setColor(hovered ? 0xaaffff : 0xffffff);
        if (!hovered)
            Gui.drawRect(x, y, x + w, y + h, 0x44ffffff);
        int offsetTextureY = this.checked ? this.textureH * 2 : 0;
        offsetTextureY += hovered ? this.textureH : 0;
        this.drawTexturedModalRect(x + 2, y, textureX, textureY + offsetTextureY, textureW, textureH);

        super.drawBackground(mouseX, mouseY);
    }

    public boolean getChecked()
    {
        return this.checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isInsideBounds(mouseX, mouseY) && type == 0)
        {
            if(!isAdventureModeRestriction() || !GuiRefinedRelocationContainer.isRestrictedAccessWithError())
            {
                setChecked(!getChecked());
                onStateChangedByUser(getChecked());
            }
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    protected void onStateChangedByUser(boolean newState)
    {
    }

    public final boolean isAdventureModeRestriction() {
        return adventureModeRestriction;
    }

    public final void setAdventureModeRestriction(boolean adventureModeRestriction) {
        this.adventureModeRestriction = adventureModeRestriction;
    }
}
