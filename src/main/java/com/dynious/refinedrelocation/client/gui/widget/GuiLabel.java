package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import org.lwjgl.opengl.GL11;

public class GuiLabel extends GuiWidgetBase
{
    public boolean drawCentered = true;
    protected String text;
    protected int color = 0x404040;
    protected boolean drawShadow = false;

    public GuiLabel(IGuiParent parent, int x, int y, String text)
    {
        super(parent, x, y, 0, 0);
        this.text = text;
        this.setSize(mc.fontRenderer.getStringWidth(this.text), mc.fontRenderer.FONT_HEIGHT);
    }

    public GuiLabel(IGuiParent parent, int x, int y, String text, int color, boolean drawShadow)
    {
        super(parent, x, y, 0, 0);
        this.text = text;
        this.setSize(mc.fontRenderer.getStringWidth(this.text), mc.fontRenderer.FONT_HEIGHT);
        this.color = color;
        this.drawShadow = drawShadow;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (this.text != null && !this.text.isEmpty())
        {
            int x = this.x;
            int y = this.y;
            if (drawCentered)
            {
                x -= mc.fontRenderer.getStringWidth(this.text) / 2;
                y -= mc.fontRenderer.FONT_HEIGHT / 2;
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.fontRenderer.drawString(this.text, x, y, this.color, this.drawShadow);
        }
        super.drawForeground(mouseX, mouseY);
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public boolean getDrawShadow()
    {
        return drawShadow;
    }

    public void setDrawShadow(boolean drawShadow)
    {
        this.drawShadow = drawShadow;
    }
}
