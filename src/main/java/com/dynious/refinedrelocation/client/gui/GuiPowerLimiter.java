package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.GuiLabel;
import com.dynious.refinedrelocation.client.gui.widget.GuiTextInputPowerLimiter;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonEnergyTypes;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonRedstoneToggle;
import com.dynious.refinedrelocation.container.ContainerPowerLimiter;
import com.dynious.refinedrelocation.helper.EnergyType;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiPowerLimiter extends GuiRefinedRelocationContainer
{
    private final TilePowerLimiter tile;
    private GuiButtonEnergyTypes btnEnergyTypes;

    public GuiPowerLimiter(TilePowerLimiter tile)
    {
        super(new ContainerPowerLimiter(tile));
        this.tile = tile;
        this.xSize = 147;
        this.ySize = 91;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        new GuiLabel(this, width / 2, height / 2 - 32, Strings.POWER_LIMITER);

        new GuiButtonRedstoneToggle(this, width / 2 - 60, height / 2 - 22, tile, MessageGUI.REDSTONE_TOGGLE);
        btnEnergyTypes = new GuiButtonEnergyTypes(this, width / 2 - 30, height / 2 - 22, 24, 20, (ContainerPowerLimiter) inventorySlots);

        new GuiLabel(this, width / 2 - 60, height / 2 + 5, Strings.MAX_ENERGY).drawCentered = false;

        new GuiTextInputPowerLimiter(this, width / 2 - 60, height / 2 + 18, 120, 20, tile, MessageGUI.POWER_LIMIT);

        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();

        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI_POWER_LIMITER);
        int xPos = (width - xSize) / 2;
        int yPos = (height - ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(f, i, j);
    }

    public EnergyType getCurrentEnergyType()
    {
        return btnEnergyTypes.getCurrentEnergyType();
    }
}
