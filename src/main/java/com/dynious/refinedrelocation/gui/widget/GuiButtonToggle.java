package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.gui.IGuiParent;

public class GuiButtonToggle extends GuiRefinedRelocationButton
{
    protected boolean state = false;
    protected GuiLabel labelTrue;
    protected GuiLabel labelFalse;
    protected int offsetXTrue = 0;
    protected int offsetYTrue = 0;
    protected int offsetXFalse = 0;
    protected int offsetYFalse = 0;

    public GuiButtonToggle(IGuiParent parent, String labelTrueText, String labelFalseText)
    {
        super(parent, null);
        labelTrue = new GuiLabel(this, x + w / 2, y + h / 2, labelTrueText, 0xffffff, true);
        labelFalse = new GuiLabel(this, x + w / 2, y + h / 2, labelFalseText, 0xffffff, true);

        this.setState(false);
    }

    public GuiButtonToggle(IGuiParent parent, int x, int y, int w, int h, int textureX, int textureY, String labelTrueText, String labelFalseText)
    {
        super(parent, x, y, w, h, textureX, textureY, null);
        labelFalse = new GuiLabel(this, x + w / 2, y + h / 2, labelFalseText, 0xffffff, true);
        labelTrue = new GuiLabel(this, x + w / 2, y + h / 2, labelTrueText, 0xffffff, true);

        this.offsetXTrue = textureX;
        this.offsetYTrue = textureY;
        this.offsetXFalse = textureX;
        this.offsetYFalse = textureY + h * 2;

        this.setState(false);
    }

    public boolean getState()
    {
        return this.state;
    }

    public void setState(boolean state)
    {
        this.state = state;

        if (this.state)
        {
            this.labelTrue.setVisible(true);
            this.labelFalse.setVisible(false);
            this.textureX = offsetXTrue;
            this.textureY = offsetYTrue;
        }
        else
        {
            this.labelFalse.setVisible(true);
            this.labelTrue.setVisible(false);
            this.textureX = offsetXFalse;
            this.textureY = offsetYFalse;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isMouseInsideBounds(mouseX, mouseY) && (type == 0 || type == 1))
        {
            setState(!getState());
            onStateChangedByUser(getState());
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    protected void onStateChangedByUser(boolean newState)
    {

    }

}