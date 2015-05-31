package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.EnergyType;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;

import java.util.List;

public class GuiButtonEnergyTypes extends GuiButton
{
    private TilePowerLimiter tile;
    private EnergyType currentEnergyType;

    public GuiButtonEnergyTypes(IGuiParent parent, TilePowerLimiter tile)
    {
        super(parent, "");
        this.tile = tile;
        if (tile != null)
        {
            setNextType();
        }
    }

    public GuiButtonEnergyTypes(IGuiParent parent, int x, int y, int w, int h, TilePowerLimiter tile)
    {
        super(parent, x, y, w, h, 0, 0, "");
        this.tile = tile;
        if (tile != null)
        {
            setNextType();
        }
    }

    public void setValue(EnergyType energyType)
    {
        this.label.setText(energyType.name());
    }

    public EnergyType getCurrentEnergyType()
    {
        return currentEnergyType;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isInsideBounds(mouseX, mouseY) && (type == 0 || type == 1))
        {
            if (tile != null)
            {
                setNextType();
            }
            else
            {
                label.setText("--");
            }
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    public void setNextType()
    {
        List<EnergyType> list = tile.getConnectionTypes();
        if (!list.isEmpty())
        {
            int ordinal = currentEnergyType != null ? currentEnergyType.ordinal() : -1;
            for (int i = ordinal + 1; i < EnergyType.values().length; i++)
            {
                EnergyType t = EnergyType.values()[i];
                if (t != EnergyType.MJ && list.contains(t))
                {
                    currentEnergyType = t;
                    setValue(t);
                    return;
                }
            }
            for (int i = 0; i < ordinal + 1; i++)
            {
                EnergyType t = EnergyType.values()[i];
                if (t != EnergyType.MJ && list.contains(t))
                {
                    currentEnergyType = t;
                    setValue(t);
                    return;
                }
            }
        }
        label.setText("--");
    }
}
