package com.dynious.blex.gui.widget;

import java.text.DecimalFormat;
import java.util.List;
import com.dynious.blex.gui.IGuiParent;

public class GuiButtonCounter extends GuiBlExButton
{
    protected double value;
    protected double min;
    protected double max;
    protected double step;
    protected double shiftStep;
    public DecimalFormat numberFormat = new DecimalFormat("##.##");

    public GuiButtonCounter(IGuiParent parent, double min, double max, double step, double shiftStep)
    {
        super(parent, "");
        this.min = min;
        this.max = max;
        this.step = step;
        this.shiftStep = shiftStep;
        setValue(min);
    }
    
    public GuiButtonCounter(IGuiParent parent, int x, int y, int w, int h, int textureX, int textureY, int min, int max, int step, int shiftStep)
    {
        super(parent, x, y, w, h, textureX, textureY, "");
        this.min = min;
        this.max = max;
        this.step = step;
        this.shiftStep = shiftStep;
        setValue(min);
    }

    public double getValue()
    {
        return this.value;
    }

    public void setValue(double value)
    {
        this.value = Math.max(min, Math.min(max, value));
        
        this.label.setText(numberFormat.format(getValue()));
    }
    
    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            tooltip.add("\u00A77Click: \u00B1"+numberFormat.format(step));
            tooltip.add("\u00A77Shift+click: \u00B1"+numberFormat.format(shiftStep));
        }
        
        return tooltip;
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isMouseInsideBounds(mouseX, mouseY) && (type == 0 || type == 1))
        {
            double oldVal = getValue();
            double curStep = (isShiftKeyDown ? shiftStep : step) * (type == 1 ? -1 : 1);
            setValue(getValue()+curStep);
            if (oldVal != getValue())
                onValueChangedByUser(getValue());
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }
    
    protected void onValueChangedByUser(double newValue)
    {
        
    }
    
}
