package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.container.ContainerPowerLimiter;
import com.dynious.refinedrelocation.helper.EnergyType;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonEnergyTypes extends GuiButton
{
    private ContainerPowerLimiter container;
    private EnergyType currentEnergyType;

    public GuiButtonEnergyTypes(IGuiParent parent, int x, int y, int w, int h, ContainerPowerLimiter container)
    {
        super(parent, x, y, w, h, 0, 0, "");
        this.container = container;
        if (container != null)
        {
            setNextType();
        }
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY))
        {
            if(currentEnergyType == null)
            {
                tooltip.add("\u00a7a" + StatCollector.translateToLocal(Strings.ENERGY_TYPE) + "\u00a7f " + StatCollector.translateToLocal(Strings.NONE));
                tooltip.add("\u00a7c" + StatCollector.translateToLocal(Strings.NO_SUITABLE_ENERGY_CONNECTION));
            } else {
                tooltip.add("\u00a7a" + StatCollector.translateToLocal(Strings.ENERGY_TYPE) + "\u00a7f " + currentEnergyType.name());
                boolean foundOne = false;
                for(int i = 0; i < container.getEnergyTypes().length; i++) {
                    if(container.getEnergyTypes()[i])
                    {
                        if (foundOne)
                        {
                            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
                            break;
                        }
                        foundOne = true;
                    }
                }
            }
        }
        return tooltip;
    }

    @Override
    public void update()
    {
        super.update();

        if(currentEnergyType == null) {
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
            if (container != null)
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
        boolean[] energyTypes = container.getEnergyTypes();
        int ordinal = currentEnergyType != null ? currentEnergyType.ordinal() : -1;
        for (int i = ordinal + 1; i < EnergyType.values().length; i++)
        {
            EnergyType t = EnergyType.values()[i];
            if (t != EnergyType.MJ && energyTypes[i])
            {
                currentEnergyType = t;
                setValue(t);
                return;
            }
        }
        for (int i = 0; i < ordinal + 1; i++)
        {
            EnergyType t = EnergyType.values()[i];
            if (t != EnergyType.MJ && energyTypes[i])
            {
                currentEnergyType = t;
                setValue(t);
                return;
            }
        }
        label.setText("--");
    }
}
