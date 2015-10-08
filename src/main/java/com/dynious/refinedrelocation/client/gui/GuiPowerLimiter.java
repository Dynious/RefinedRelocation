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
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiPowerLimiter extends GuiRefinedRelocationContainer {
    private final TilePowerLimiter tile;
    private GuiButtonEnergyTypes btnEnergyTypes;

    public GuiPowerLimiter(TilePowerLimiter tile) {
        super(new ContainerPowerLimiter(tile));
        this.tile = tile;
        xSize = 147;
        ySize = 91;
    }

    @Override
    public void initGui() {
        super.initGui();

        new GuiLabel(this, width / 2, height / 2 - 32, StatCollector.translateToLocal(Strings.POWER_LIMITER));

        new GuiButtonRedstoneToggle(this, width / 2 - 60, height / 2 - 22, tile);
        btnEnergyTypes = new GuiButtonEnergyTypes(this, width / 2 - 30, height / 2 - 22, (ContainerPowerLimiter) inventorySlots);

        new GuiLabel(this, width / 2 - 60, height / 2 + 5, StatCollector.translateToLocal(Strings.MAX_ENERGY)).drawCentered = false;

        new GuiTextInputPowerLimiter(this, width / 2 - 60, height / 2 + 18, 120, 20, tile).setEnabled(isRestrictedAccess());

        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        drawWindow(guiLeft, guiTop, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(f, i, j);
    }

    public EnergyType getCurrentEnergyType() {
        return btnEnergyTypes.getCurrentEnergyType();
    }
}
