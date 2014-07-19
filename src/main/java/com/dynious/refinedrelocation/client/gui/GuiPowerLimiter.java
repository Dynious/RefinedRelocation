package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.GuiButtonEnergyTypes;
import com.dynious.refinedrelocation.client.gui.widget.GuiButtonRedstoneToggle;
import com.dynious.refinedrelocation.client.gui.widget.GuiTextInputPowerLimiter;
import com.dynious.refinedrelocation.container.ContainerPowerLimiter;
import com.dynious.refinedrelocation.helper.EnergyType;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import org.lwjgl.opengl.GL11;

public class GuiPowerLimiter extends GuiRefinedRelocationContainer
{
    private TilePowerLimiter tile;
    private GuiButtonEnergyTypes button;

    public GuiPowerLimiter(TilePowerLimiter tile)
    {
        super(new ContainerPowerLimiter(tile));
        this.tile = tile;
        this.xSize = 123;
        this.ySize = 45;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        button = new GuiButtonEnergyTypes(this, width / 2 + 30, height / 2 - 10, 24, 20, tile);
        new GuiTextInputPowerLimiter(this, width / 2 - 20, height / 2 - 10, 40, 20, tile);

        new GuiButtonRedstoneToggle(this, width / 2 - 54, height / 2 - 10, tile);
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
        return button.getCurrentEnergyType();
    }
}
