package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.gui.IGuiParent;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextInput extends GuiWidgetBase
{
    protected GuiTextField textField;

    public GuiTextInput(IGuiParent parent, int x, int y, int w, int h)
    {
        super(parent, x, y, w, h);
        this.textField = new GuiTextField(mc.fontRenderer, x, y, w, h);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
        this.textField.mouseClicked(mouseX, mouseY, type);
    }

    @Override
    public boolean keyTyped(char c, int i)
    {
        if (super.keyTyped(c, i))
            return true;

        String lastText = this.textField.getText();
        this.textField.textboxKeyTyped(c, i);
        if (!lastText.equals(this.textField.getText()))
        {
            onTextChangedByUser(this.textField.getText());
            return true;
        }
        else
            return false;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        this.textField.drawTextBox();
        super.drawBackground(mouseX, mouseY);
    }

    protected void onTextChangedByUser(String newText)
    {

    }

    // Begin delegate methods

    public void updateCursorCounter()
    {
        textField.updateCursorCounter();
    }

    public void deleteWords(int par1)
    {
        textField.deleteWords(par1);
    }

    public void deleteFromCursor(int par1)
    {
        textField.deleteFromCursor(par1);
    }

    public void setText(String par1Str)
    {
        textField.setText(par1Str);
    }

    public String getText()
    {
        return textField.getText();
    }

    public String getSelectedtext()
    {
        return textField.getSelectedtext();
    }

    public void writeText(String par1Str)
    {
        textField.writeText(par1Str);
    }

    public int getNthWordFromCursor(int par1)
    {
        return textField.getNthWordFromCursor(par1);
    }

    public int getNthWordFromPos(int par1, int par2)
    {
        return textField.getNthWordFromPos(par1, par2);
    }

    public int func_73798_a(int par1, int par2, boolean par3)
    {
        return textField.func_73798_a(par1, par2, par3);
    }

    public void moveCursorBy(int par1)
    {
        textField.moveCursorBy(par1);
    }

    public void setCursorPosition(int par1)
    {
        textField.setCursorPosition(par1);
    }

    public void setCursorPositionZero()
    {
        textField.setCursorPositionZero();
    }

    public void setCursorPositionEnd()
    {
        textField.setCursorPositionEnd();
    }

    public void setMaxStringLength(int par1)
    {
        textField.setMaxStringLength(par1);
    }

    public int getMaxStringLength()
    {
        return textField.getMaxStringLength();
    }

    public int getCursorPosition()
    {
        return textField.getCursorPosition();
    }

    public boolean getEnableBackgroundDrawing()
    {
        return textField.getEnableBackgroundDrawing();
    }

    public void setEnableBackgroundDrawing(boolean par1)
    {
        textField.setEnableBackgroundDrawing(par1);
    }

    public void setTextColor(int par1)
    {
        textField.setTextColor(par1);
    }

    public void setDisabledTextColour(int par1)
    {
        textField.setDisabledTextColour(par1);
    }

    public void setFocused(boolean par1)
    {
        textField.setFocused(par1);
    }

    public boolean isFocused()
    {
        return textField.isFocused();
    }

    public void setEnabled(boolean par1)
    {
        textField.setEnabled(par1);
    }

    public int getSelectionEnd()
    {
        return textField.getSelectionEnd();
    }

    public int getWidth()
    {
        return textField.getWidth();
    }

    public void setSelectionPos(int par1)
    {
        textField.setSelectionPos(par1);
    }

    public void setCanLoseFocus(boolean par1)
    {
        textField.setCanLoseFocus(par1);
    }

    public boolean getTextFieldVisible()
    {
        return textField.getVisible();
    }

    public void setTextFieldVisible(boolean par1)
    {
        textField.setVisible(par1);
    }

    // End delegate methods

}
