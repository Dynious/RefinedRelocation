package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.gui.GuiPowerLimiter;
import com.dynious.refinedrelocation.helper.EnergyType;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.network.packet.PacketSetMaxPower;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiTextInputPowerLimiter extends GuiTextInput
{
    private TilePowerLimiter tile;
    private double maxAcceptedEnergy;
    protected GuiPowerLimiter parent;
    private EnergyType currentEnergyType;

    public GuiTextInputPowerLimiter(GuiPowerLimiter parent, int x, int y, int w, int h, TilePowerLimiter tile)
    {
        super(parent, x, y, w, h);
        this.tile = tile;
        this.parent = parent;
        update();
    }

    @Override
    protected void onTextChangedByUser(String newFilter)
    {
        if (tile == null)
            return;

        maxAcceptedEnergy = stringToMaxEnergy(newFilter);
        tile.setMaxAcceptedEnergy(maxAcceptedEnergy);
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketSetMaxPower(maxAcceptedEnergy)));
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
            if (tile.getMaxAcceptedEnergy() != maxAcceptedEnergy)
            {
                maxAcceptedEnergy = tile.getMaxAcceptedEnergy();
                setText(maxEnergyToString(tile.getMaxAcceptedEnergy()));
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
        if (parent.getCurrentEnergyType() == null)
        {
            return "--";
        }
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
        }
        catch (NumberFormatException e)
        {
            power = 0.0D;
        }
        return parent.getCurrentEnergyType().convertToInternal(power);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> list = new ArrayList<String>();
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            list.add(StatCollector.translateToLocal(Strings.MAX_ENERGY));
        }
        return list;
    }
}
