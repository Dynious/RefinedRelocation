package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.EnergyType;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.network.packet.PacketSetMaxPower;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import cpw.mods.fml.common.network.PacketDispatcher;
import org.apache.commons.lang3.StringUtils;

public class GuiTextInputPowerLimiter extends GuiTextInput
{
    private TilePowerLimiter tile;
    private double maxAcceptedEnergy;

    public GuiTextInputPowerLimiter(IGuiParent parent, int x, int y, int w, int h, TilePowerLimiter tile)
    {
        super(parent, x, y, w, h);
        this.tile = tile;
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
        if (tile != null && tile.getMaxAcceptedEnergy() != maxAcceptedEnergy)
        {
            maxAcceptedEnergy = tile.getMaxAcceptedEnergy();
            setText(maxEnergyToString(tile.getMaxAcceptedEnergy()));
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
        return Double.toString(EnergyType.EU.fromInternal(maxAcceptedEnergy));
    }

    public double stringToMaxEnergy(String string)
    {
        if (string.isEmpty())
        {
            return 0;
        }
        return EnergyType.EU.convertToInternal(Double.parseDouble(string));
    }
}
