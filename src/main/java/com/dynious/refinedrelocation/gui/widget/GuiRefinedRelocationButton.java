package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Resources;
import org.lwjgl.opengl.GL11;

public class GuiRefinedRelocationButton extends GuiRefinedRelocationWidgetBase
{
    protected int textureX = 0;
    protected int textureY = 0;
    protected GuiLabel label = null;

    public GuiRefinedRelocationButton(IGuiParent parent, String labelText)
    {
        super(parent);
        this.label = new GuiLabel(this, x + w / 2, y + h / 2, labelText, 0xffffff, true);
    }

    public GuiRefinedRelocationButton(IGuiParent parent, int x, int y, int w, int h, int textureX, int textureY, String labelText)
    {
        super(parent, x, y, w, h);
        this.textureX = textureX;
        this.textureY = textureY;
        this.label = new GuiLabel(this, this.x + this.w / 2, this.y + this.h / 2, labelText, 0xffffff, true);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        mc.getTextureManager().bindTexture(Resources.GUI_SHARED);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int offsetTextureY = 0;
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            offsetTextureY += this.h;
        }
        this.drawTexturedModalRect(x, y, textureX, textureY + offsetTextureY, w, h);

        for (IGuiRefinedRelocationWidgetBase child : this.children)
        {
            if (child instanceof GuiLabel)
            {
                GuiLabel childLabel = (GuiLabel) child;
                childLabel.setColor(isMouseInsideBounds(mouseX, mouseY) ? 0xffffa0 : 0xffffff);
            }
        }

        super.drawBackground(mouseX, mouseY);
    }

    @Override
    public void moveY(int amount)
    {
        super.moveY(amount);
        if (label != null)
            label.moveY(amount);
    }

    @Override
    public void moveX(int amount)
    {
        super.moveX(amount);
        if (label != null)
            label.moveX(amount);
    }

    @Override
    public void setPos(int x, int y)
    {
        super.setPos(x, y);
        if (label != null)
            label.setPos(x + w / 2, y + h / 2);
    }
}
