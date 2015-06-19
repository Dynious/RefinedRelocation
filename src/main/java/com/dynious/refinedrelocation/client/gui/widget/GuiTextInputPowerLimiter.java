package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.GuiPowerLimiter;
import com.dynious.refinedrelocation.helper.EnergyType;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.StringUtils;

public class GuiTextInputPowerLimiter extends GuiTextInput
{
    private final int boundMessageId;
    protected GuiPowerLimiter parent;
    private TilePowerLimiter tile;
    private double maxAcceptedEnergy;
    private EnergyType currentEnergyType;

    public GuiTextInputPowerLimiter(GuiPowerLimiter parent, int x, int y, int w, int h, TilePowerLimiter tile, int boundMessageId)
    {
        super(parent, x, y, w, h);
        this.boundMessageId = boundMessageId;
        this.tile = tile;
        this.parent = parent;
        update();
    }

    @Override
    protected void onTextChangedByUser(String text)
    {
        if (tile == null)
        {
            return;
        }

        maxAcceptedEnergy = stringToMaxEnergy(text);
        tile.setMaxAcceptedEnergy(maxAcceptedEnergy);
        GuiHelper.sendDoubleMessage(boundMessageId, maxAcceptedEnergy);
    }

    @Override
    public void update()
    {
        if (tile != null)
        {
            if (currentEnergyType != parent.getCurrentEnergyType())
            {
                currentEnergyType = parent.getCurrentEnergyType();
                maxAcceptedEnergy = -1.0D;
            }
            if(currentEnergyType == null) {
                setText("\u00a7c" + StatCollector.translateToLocal(Strings.NO_ENERGY_CONNECTION));
                setEnabled(false);
            } else
            {
                setEnabled(true);
                if (tile.getMaxAcceptedEnergy() != maxAcceptedEnergy)
                {
                    maxAcceptedEnergy = tile.getMaxAcceptedEnergy();
                    setText(maxEnergyToString(tile.getMaxAcceptedEnergy()));
                }
            }
        }

        super.update();
    }

    @Override
    public boolean keyTyped(char c, int i)
    {
        String lastText = this.textField.getText();
        if (Character.isDigit(c) || Character.getType(c) == 15 || (Character.getType(c) == 24 && !StringUtils.contains(lastText, 24)))
        {
            this.textField.textboxKeyTyped(c, i);
            if (!lastText.equals(this.textField.getText()))
            {
                onTextChangedByUser(this.textField.getText());
                return true;
            }
        }
        return false;
    }

    public String maxEnergyToString(double maxAcceptedEnergy)
    {
        return Double.toString(parent.getCurrentEnergyType().fromInternal(maxAcceptedEnergy));
    }

    public double stringToMaxEnergy(String string)
    {
        if (string.isEmpty() || parent.getCurrentEnergyType() == null)
        {
            return 0;
        }
        double power;
        try
        {
            power = Double.parseDouble(string);
        } catch (NumberFormatException e)
        {
            power = 0.0D;
        }
        return parent.getCurrentEnergyType().convertToInternal(power);
    }

}
