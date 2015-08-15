package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.GuiRefinedRelocationContainer;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

import java.text.DecimalFormat;
import java.util.List;

public class GuiButtonCounter extends GuiButton
{
    public DecimalFormat numberFormat = new DecimalFormat("##.##");
    protected double value;
    protected double min;
    protected double max;
    protected double step;
    protected double shiftStep;

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

        if (isInsideBounds(mouseX, mouseY))
        {
            tooltip.add("\u00A77" + StatCollector.translateToLocal(Strings.CLICK) + ": \u00B1" + numberFormat.format(step));
            tooltip.add("\u00A77" + StatCollector.translateToLocal(Strings.SHIFT_CLICK) + ": \u00B1" + numberFormat.format(shiftStep));
        }

        return tooltip;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isInsideBounds(mouseX, mouseY) && (type == 0 || type == 1))
        {
            if(!isAdventureModeRestriction() || !GuiRefinedRelocationContainer.isRestrictedAccessWithError())
            {
                double oldVal = getValue();
                double curStep = (isShiftKeyDown ? shiftStep : step) * (type == 1 ? -1 : 1);
                setValue(getValue() + curStep);
                if (oldVal != getValue())
                {
                    onValueChangedByUser(getValue());
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    protected void onValueChangedByUser(double newValue)
    {
    }

}
